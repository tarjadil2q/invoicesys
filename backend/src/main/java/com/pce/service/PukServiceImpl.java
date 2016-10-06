package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.domain.Pce;
import com.pce.domain.Puk;
import com.pce.domain.PukGroup;
import com.pce.domain.PukItem;
import com.pce.repository.PukItemRepository;
import com.pce.repository.PukRepository;
import com.pce.service.mapper.PukMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Year;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Service
public class PukServiceImpl implements PukService {

  @Autowired
  private PukMapper pukMapper;

  @Autowired
  private PukRepository pukRepository;

  @Autowired
  private PukItemRepository pukItemRepository;

  @Autowired
  private PukGroupService pukGroupService;

  @Override
  public Page<Puk> getAllAvailablePuk(Pageable pageRequest) {
    return pukRepository.findAll(pageRequest);
  }

  @Override
  @Transactional
  public Puk createOrUpdatePuk(Puk puk) {
    Preconditions.checkArgument(puk != null, new IllegalArgumentException("Puk cannot be null"));
    Preconditions.checkArgument(puk.getPukGroup() != null, new IllegalArgumentException("Puk Group cannot be null"));
    Set<PukItem> pukItems = puk.getPukItems();
    List<PukItem> existingPukItems = getExistingPukItem(puk.getPukId(), pukItems);

    BigDecimal totalBudget = BigDecimal.ZERO;
    if (!CollectionUtils.isEmpty(pukItems)) {
      for (PukItem pukItem : pukItems) {
        calculateTotalPriceFor(pukItem);
        pukItem.setPuk(puk);

        totalBudget = totalBudget.add(pukItem.getTotalPrice());
      }
    }

    for (PukItem existingPukItem : existingPukItems) {
      totalBudget = totalBudget.add(existingPukItem.getTotalPrice());
    }


    puk.setBudget(totalBudget);
    puk.setPukYear(Year.now().getValue());
    Puk savedPuk = pukRepository.save(puk);
    Set<PukItem> savedPukItems = savedPuk.getPukItems();
    if (!CollectionUtils.isEmpty(savedPukItems)) {
      CopyOnWriteArrayList<PukItem> copyOfPukItems = new CopyOnWriteArrayList<>(savedPukItems);

      pukItemRepository.save(copyOfPukItems);
    }

    return savedPuk;
  }

  private void calculateTotalPriceFor(PukItem pukItem) {
    pukItem.setTotalPrice(pukItem.getPerMeasurementPrice()
            .multiply(new BigDecimal(pukItem.getQuantity()))
            .multiply(new BigDecimal(pukItem.getTotalActivity())));
  }

  @Transactional
  public PukItem createOrUpdatePukItem(Puk puk
          , PukItem pukItem) {
    calculateTotalPriceFor(pukItem);
    pukItem.setPuk(puk);

    PukItem savedPukItem = pukItemRepository.save(pukItem);
    List<PukItem> pukItems = pukItemRepository.findByPukId(puk.getPukId());
    BigDecimal totalBudget = BigDecimal.ZERO;
    for (PukItem puItem : pukItems) {
      totalBudget = totalBudget.add(puItem.getTotalPrice());
    }
    puk.setBudget(totalBudget);
    pukRepository.save(puk);
    return savedPukItem;

  }

  //TODO change to lambda
  private List<PukItem> getExistingPukItem(long pukId, Set<PukItem> currentPukItem) {
    List<PukItem> existingPukItems = pukItemRepository.findByPukId(pukId);
    Iterator<PukItem> existingPukIterators = existingPukItems.iterator();
    while (existingPukIterators.hasNext()) {
      PukItem pi = existingPukIterators.next();
      if (CollectionUtils.isEmpty(currentPukItem)) {
        break;
      }
      for (PukItem pukItem : currentPukItem) {
        if (pi.getPukItemId() == pukItem.getPukItemId()) {
          existingPukIterators.remove();
        }
      }
    }
    return existingPukItems;
  }

  @Override
  public Optional<PukItem> getPukItemByPukItemId(long id) {
    return Optional.ofNullable(pukItemRepository.findOne(id));
  }

  public Optional<PukItem> getPukItemByPukIdAndPukItemId(long pukId, long pukItemId) {
    return Optional.ofNullable(pukItemRepository.findByPukIdAndPukItemId(pukId, pukItemId));
  }

  @Override
  public Page<Puk> getPuksForPukGroup(PukGroup pukGroup, Pageable pageRequest) {
    Preconditions.checkArgument(pukGroup != null, "Puk Group cannot be null");
    return pukRepository.findByPukGroup(pukGroup, pageRequest);
  }

  @Override
  public Optional<Puk> getPukByPukNoIgnoreCase(String pukNo) {
    return Optional.ofNullable(pukRepository.findByPukNoIgnoreCase(pukNo));
  }

  @Override
  public boolean isPukExist(long id) {
    return pukRepository.exists(id);
  }

  @Override
  public Optional<Puk> getPukByPukId(long id) {
    return Optional.ofNullable(pukRepository.findOne(id));
  }

  @Override
  public Optional<Puk> getPukByPce(Pce pce) {
    Preconditions.checkArgument(pce != null, "Pce cannot be null");
    Set<Pce> associatedPces = new HashSet<>();
    associatedPces.add(pce);
    return Optional.ofNullable(pukRepository.findByAssociatedPces(associatedPces));
  }

  @Override
  public Page<PukItem> getPukItemsByPukId(long pukId, Pageable pageRequest) {
    return pukItemRepository.findByPukId(pukId, pageRequest);
  }
}
