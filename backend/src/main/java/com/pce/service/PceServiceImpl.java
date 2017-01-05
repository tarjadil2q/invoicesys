package com.pce.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Sets;
import com.pce.domain.*;
import com.pce.exception.PceBudgetException;
import com.pce.exception.PukException;
import com.pce.repository.PceItemRepository;
import com.pce.repository.PceRepository;
import com.pce.repository.PukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
@Service
public class PceServiceImpl implements PceService {

  @Autowired
  private PceRepository pceRepository;

  @Autowired
  private PceItemRepository pceItemRepository;

  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;

  @Autowired
  private PukRepository pukRepository;

  @Autowired
  private PukGroupService pukGroupService;

  @Autowired
  private PukService pukService;

  @Autowired
  private UserService userService;

  @Autowired
  private DriveService driveService;


  public Page<Pce> getAllAvailablePce(Pageable pageRequest) {
    return pceRepository.findAll(pageRequest);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Pce createOrUpdatePce(Pce pce) {
    Preconditions.checkArgument(pce != null, new IllegalArgumentException("Pce cannot be null"));
    Set<PceItem> pceItems = pce.getPceItems();
    BigDecimal totalPcePrice = getPceTotalPriceBy(pce, pceItems);
    Puk assocPuk = pce.getAssociatedPuk();
    Optional<Puk> pukByPukId = pukService.getPukByPukId(assocPuk.getPukId());
    if (!pukByPukId.isPresent()) {
      throw new PukException("Puk with ID " + assocPuk.getPukId() + " cannot be found" + " please ensure the puk is valid");
    }
    BigDecimal pukBudget = pukByPukId.get().getBudget();
    BigDecimal totalPceBudgetBy = getTotalPceBudgetBy(assocPuk);
    BigDecimal totalPceAmountSoFar = totalPcePrice.add(totalPceBudgetBy == null ? new BigDecimal(0) : totalPceBudgetBy);
    if (totalPceAmountSoFar.compareTo(pukBudget) == 1) {
      throw new PceBudgetException("Total Puk Budget " + pukBudget + "  for " + assocPuk.getPukDescription() + " is not enough against total amount PCE so far for this puk  " + totalPcePrice);
    }

    pce.setTotalAmount(totalPcePrice);
    if (pce.getPceYear() == 0) {
      pce.setPceYear(Year.now().getValue());
    }

    if (StringUtils.isEmpty(pce.getPceNo())) {

      long pukId = assocPuk.getPukId();
      int maxPceCountByYear = pceRepository.findMaxPceCountByYear(pce.getPceYear(), pukId);
      maxPceCountByYear = maxPceCountByYear + 1;
      Puk associatedPuk = pukRepository.findOne(pukId);
      String pukNo = associatedPuk.getPukNo();
      pce.setPceNo(pukNo + "-" + maxPceCountByYear + "-" + associatedPuk.getPukYear());
    }


    Pce savedPce = pceRepository.save(pce);
    Set<PceItem> pceItemSet = savedPce.getPceItems();
    if (!CollectionUtils.isEmpty(pceItemSet)) {
      CopyOnWriteArrayList<PceItem> copyOfPceItems = new CopyOnWriteArrayList<>(pceItemSet);
      copyOfPceItems.stream().forEach(pceItem -> pceItem.setPce(savedPce));
      pceItemRepository.save(copyOfPceItems);
    }

    return savedPce;

  }

  private BigDecimal getPceTotalPriceBy(Pce pce, Set<PceItem> currentPceItems) {
    List<PceItem> existingPceItems = getExistingPceItem(pce.getPceId(), currentPceItems);
    BigDecimal totalPcePrice = BigDecimal.ZERO;
    if (!CollectionUtils.isEmpty(currentPceItems)) {
      for (PceItem pceItem : currentPceItems) {
        totalPcePrice = totalPcePrice.add(pceItem.getPriceAmount());
      }
    }


    for (PceItem existingPceItem : existingPceItems) {
      totalPcePrice = totalPcePrice.add(existingPceItem.getPriceAmount());
    }
    return totalPcePrice;
  }

  private BigDecimal getPceTotalPrice(Pce pce) {
    Preconditions.checkArgument(pce != null, "Pce cannot be null");
    Set<PceItem> pceItems = pce.getPceItems();
    Preconditions.checkArgument(pceItems != null, "Pce Item cannot be null");
    BigDecimal totalPcePrice = BigDecimal.ZERO;
    for (PceItem pceItem : pceItems) {
      totalPcePrice = totalPcePrice.add(pceItem.getPriceAmount());
    }
    return totalPcePrice;

  }

  public BigDecimal getTotalPceBudgetBy(Puk puk) {
    return pceRepository.findTotalPceAmountByPuk(puk.getPukId());
  }

  //TODO change to lambda
  private List<PceItem> getExistingPceItem(long pceId, Set<PceItem> currentPceItem) {
    List<PceItem> existingPceItems = pceItemRepository.findByPceId(pceId);
    Iterator<PceItem> existingPceIterators = existingPceItems.iterator();
    while (existingPceIterators.hasNext()) {
      PceItem pceItem = existingPceIterators.next();
      if (CollectionUtils.isEmpty(currentPceItem)) {
        break;
      }
      for (PceItem pceItm : currentPceItem) {
        if (pceItem.getPceItemId() == pceItm.getPceItemId()) {
          existingPceIterators.remove();
        }
      }
    }
    return existingPceItems;
  }

  @Override
  public Optional<Pce> getPceByPceId(long id) {
    return Optional.ofNullable(pceRepository.findOne(id));
  }


  @Transactional
  @Override
  public PceItem createOrUpdatePceItem(Pce pce, PceItem pceItem) {
    pceItem.setPce(pce);
    BigDecimal totalPcePrice = getPceTotalPriceBy(pce, Sets.newHashSet(pceItem));
    Puk assocPuk = pce.getAssociatedPuk();
    BigDecimal pukBudget = assocPuk.getBudget();
    if (totalPcePrice.compareTo(pukBudget) == 1) {
      throw new PceBudgetException("Total Puk Budget " + pukBudget + "  for " + assocPuk.getPukDescription() + " is not enough against total amount PCE so far for this puk  " + totalPcePrice);
    }
    PceItem savedPceItem = pceItemRepository.save(pceItem);
    pce.setTotalAmount(totalPcePrice);
    pceRepository.save(pce);
    return savedPceItem;
  }


  @Override
  public Optional<PceItem> getPceItemByPceIdAndPceItemId(long pceId, long PceItemId) {
    return Optional.ofNullable(pceItemRepository.findByPceIdAndPceItemId(pceId, PceItemId));
  }

  @Override
  public Page<Pce> findAllPceToBeApproved(CurrentUser currentUser, Pageable pageRequest) {
    List<Role> validRoles = getAllApprovalRoleOrderBySequence();
    Role approvalRoleForCurrentUser = findApprovalRoleFrom(currentUser, validRoles);
    if (approvalRoleForCurrentUser == null) {
      return null;
    }
    List<PukGroup> allPukGroupsForCurrentUser = pukGroupService.getAllAvailablePukGroupForCurrentUser(currentUser);
    List<Puk> puksForPukGroups = pukService.getPuksForPukGroups(allPukGroupsForCurrentUser);

    int indexOfCurrentRole = validRoles.indexOf(approvalRoleForCurrentUser);
    int currentYear = Year.now().getValue();
    if (indexOfCurrentRole == 0) {
      return pceRepository.findByApproversIsNullAndAssociatedPukInAndPceYearOrderByCreationDateDesc(puksForPukGroups, currentYear, pageRequest);
    }
    Role previousApprovalRole = validRoles.get(indexOfCurrentRole - 1);

    Optional<User> previousApproverUser = userService.getUserByRole(previousApprovalRole);
    if (!previousApproverUser.isPresent()) {
      return null;
    }
    Page<Pce> pceThatHaveBeenApprovedByPreviousUser = pceRepository.findByApproversInAndAssociatedPukInAndPceYearOrderByCreationDateDesc(previousApproverUser.get(), puksForPukGroups, currentYear, pageRequest);

    List<Pce> pces = pceThatHaveBeenApprovedByPreviousUser.getContent();
    List<Pce> pceToBeApprovedByCurrentUser = new ArrayList<>();
    if (!CollectionUtils.isEmpty(pces)) {
      for (Pce pce : pces) {
        List<Long> idCollects = pce.getApprovers().stream().map(user -> user.getId()).collect(Collectors.toList());
        if (!idCollects.contains(currentUser.getId())) {
          pceToBeApprovedByCurrentUser.add(pce);
        }
      }
    }
    return new PageImpl<>(pceToBeApprovedByCurrentUser);
  }

  private Role findApprovalRoleFrom(CurrentUser currentUser, List<Role> validApprovalRoles) {
    Set<Role> roles = currentUser.getRoles();
    for (Role role : roles) {
      for (Role validRole : validApprovalRoles) {
        if (role.getId() == validRole.getId()) {
          return validRole;
        }
      }
    }
    return null;
  }


  private List<Role> getAllApprovalRoleOrderBySequence() {
    List<PceApprovalRole> validApprovalRoles = pceApprovalRoleService.findAllAvailableApprovalRoleOrderBySequenceNoAsc();
    return validApprovalRoles.stream().map(pceRole -> pceRole.getPceApprovalRole()).collect(Collectors.toList());
  }

  @Override
  public boolean approvePce(Pce pce, CurrentUser currentUser) {
    Set<Role> roles = currentUser.getRoles();

    Set<User> currentApprovers = pce.getApprovers();
    List<Role> listOfValidRole = getAllApprovalRoleOrderBySequence();
    if (CollectionUtils.isEmpty(currentApprovers)) {
      if (roles.stream().anyMatch(role -> role.getId() == listOfValidRole.get(0).getId())) {
        Set<User> approvers = pce.getApprovers();
        approvers.add(currentUser.getUser());
        pceRepository.save(pce);
        return true;
      }

    }

    Role nextApprovalRole = getNextApproverOrRejecterRole(currentApprovers, listOfValidRole);
    if (nextApprovalRole == null) {
      return false;
    }
    if (currentUser.getRoles().stream().anyMatch(role -> role.getId() == nextApprovalRole.getId())) {
      Set<User> approvers = pce.getApprovers();
      approvers.add(currentUser.getUser());
      pceRepository.save(pce);
      return true;
    }
    return false;
  }

  public Role getNextApproverOrRejecterRole(Set<User> currentApprovers, List<Role> listOfValidRole) {
    Set<Role> currentApproverRoles = new LinkedHashSet<>();
    for (User user : currentApprovers) {
      currentApproverRoles.addAll(user.getRoles());
    }
    Set<Long> currentApproverRoleIds = currentApproverRoles.stream().map(roleUser -> roleUser.getId()).collect(Collectors.toSet());
    List<Role> nextApproverOrRejecterRoles = listOfValidRole.stream().filter(role -> !currentApproverRoleIds.contains(role.getId())).collect(Collectors.toList());
    if (!CollectionUtils.isEmpty(nextApproverOrRejecterRoles)) {
      return nextApproverOrRejecterRoles.get(0);
    }
    return null;
  }


  @Override
  public boolean rejectPce(Pce pce, CurrentUser currentUser) {
    List<Role> validRoles = pceApprovalRoleService.findAllAvailableApprovalRoleOrderBySequenceNoAsc().stream().map(pceApprovalRole -> pceApprovalRole.getPceApprovalRole()).collect(Collectors.toList());
    Role nextApproverOrRejecterRole = getNextApproverOrRejecterRole(pce.getApprovers(), validRoles);
    if (nextApproverOrRejecterRole == null) {
      return false;
    }
    if (currentUser.getRoles().stream().anyMatch(role -> role.getId() == nextApproverOrRejecterRole.getId())) {
      pce.setApprovers(new HashSet<>());
      pceRepository.save(pce);
      return true;
    }
    return false;

  }

  @Override
  public Page<PceItem> getPceItemsByPce(Pce pce, Pageable pageRequest) {
    Preconditions.checkArgument(pce != null, "Pce cannot be null");
    return pceItemRepository.findByPce(pce, pageRequest);
  }

  @Override
  public Page<Pce> getAvailablePceByPukId(long pukId, Pageable pageRequest) {
    return pceRepository.findByPukId(pukId, pageRequest);
  }

  public boolean deletePceItemAndPce(long pceId, long pceItemId) {
    Pce pce = pceRepository.findOne(pceId);
    if (pce != null && pce.getApprovers().isEmpty()) {
      if (pceItemRepository.findByPceIdAndPceItemId(pceId, pceItemId) != null) {

        pceItemRepository.delete(pceItemId);
        BigDecimal pceTotalPrice = getPceTotalPrice(pce);
        pce.setTotalAmount(pceTotalPrice);
        pceRepository.save(pce);
        return true;
      }
    }
    return false;
  }

  public boolean deletePce(long pceId) {
    Pce pce = pceRepository.findOne(pceId);
    Set<GDriveFile> driveFiles = pce.getDriveFiles();
    if (pce != null && pce.getApprovers().isEmpty()) {
      if (driveFiles != null) {
        driveFiles.forEach(gDriveFile -> driveService.deleteDriveFile(gDriveFile.getgDriveFileId()));
      }
      pceRepository.delete(pceId);
      return true;
    }
    return false;
  }
}
