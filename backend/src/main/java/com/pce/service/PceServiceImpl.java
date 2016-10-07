package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.domain.*;
import com.pce.repository.PceItemRepository;
import com.pce.repository.PceRepository;
import com.pce.repository.PukRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
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

  public Page<Pce> getAllAvailablePce(Pageable pageRequest) {
    return pceRepository.findAll(pageRequest);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE)
  public Pce createOrUpdatePce(Pce pce) {
    Preconditions.checkArgument(pce != null, new IllegalArgumentException("Pce cannot be null"));
    Set<PceItem> pceItems = pce.getPceItems();
    List<PceItem> existingPukItems = getExistingPceItem(pce.getPceId(), pceItems);
    BigDecimal totalPcePrice = BigDecimal.ZERO;
    if (!CollectionUtils.isEmpty(pceItems)) {
      for (PceItem pceItem : pceItems) {
        totalPcePrice = totalPcePrice.add(pceItem.getPriceAmount());
      }
    }

    for (PceItem existingPceItem : existingPukItems) {
      totalPcePrice = totalPcePrice.add(existingPceItem.getPriceAmount());
    }
    pce.setTotalAmount(totalPcePrice);
    if (pce.getPceYear() == 0) {
      pce.setPceYear(Year.now().getValue());
    }

    if (StringUtils.isEmpty(pce.getPceNo())) {

      long pukId = pce.getAssociatedPuk().getPukId();
      int maxPceCountByYear = pceRepository.findMaxPceCountByYear(pce.getPceYear(), pukId);
      maxPceCountByYear = maxPceCountByYear + 1;
      Puk associatedPuk = pukRepository.findOne(pukId);
      String pukGroupName = associatedPuk.getPukGroup().getPukGroupName();
      pce.setPceNo(pukGroupName + "-" + maxPceCountByYear);
    }


    Pce savedPce = pceRepository.save(pce);
    Set<PceItem> pceItemSet = savedPce.getPceItems();
    if (!CollectionUtils.isEmpty(pceItemSet)) {
      CopyOnWriteArrayList<PceItem> copyOfPceItems = new CopyOnWriteArrayList<>(pceItemSet);

      pceItemRepository.save(copyOfPceItems);
    }

    return savedPce;

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

    PceItem savedPceItem = pceItemRepository.save(pceItem);
    List<PceItem> pceItems = pceItemRepository.findByPceId(pce.getPceId());
    BigDecimal totalPcePrice = BigDecimal.ZERO;
    for (PceItem pci : pceItems) {
      totalPcePrice = totalPcePrice.add(pci.getPriceAmount());
    }
    pce.setTotalAmount(totalPcePrice);
    pceRepository.save(pce);
    return savedPceItem;
  }

  @Override
  public Optional<PceItem> getPceItemByPceIdAndPceItemId(long pceId, long PceItemId) {
    return Optional.ofNullable(pceItemRepository.findByPceIdAndPceItemId(pceId, PceItemId));
  }

  @Override
  public boolean approvePce(Pce pce, CurrentUser currentUser) {
    Set<Role> roles = currentUser.getRoles();
    List<PceApprovalRole> validApprovalRoles = pceApprovalRoleService.findAllAvailableApprovalRoleOrderBySequenceNoAsc();
    Set<User> currentApprovers = pce.getApprovers();
    List<Role> listOfValidRole = validApprovalRoles.stream().map(pceRole -> pceRole.getPceApprovalRole()).collect(Collectors.toList());
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

  private Role getNextApproverOrRejecterRole(Set<User> currentApprovers, List<Role> listOfValidRole) {
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
}
