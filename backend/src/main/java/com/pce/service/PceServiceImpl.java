package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.domain.Pce;
import com.pce.domain.PceItem;
import com.pce.repository.PceItemRepository;
import com.pce.repository.PceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
@Service
public class PceServiceImpl implements PceService {

  @Autowired
  private PceRepository pceRepository;

  @Autowired
  private PceItemRepository pceItemRepository;

  public Page<Pce> getAllAvailablePce(Pageable pageRequest) {
    return pceRepository.findAll(pageRequest);
  }

  @Transactional
  @Autowired
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
    pce.setPceYear(Year.now().getValue());

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

  @Override
  public Optional<PceItem> getPukItemByPukItemId(long id) {
    return Optional.ofNullable(pceItemRepository.findOne(id));
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
}
