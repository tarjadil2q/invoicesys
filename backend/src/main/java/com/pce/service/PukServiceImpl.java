package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.domain.Puk;
import com.pce.domain.PukItem;
import com.pce.repository.PukItemRepository;
import com.pce.repository.PukRepository;
import com.pce.service.mapper.PukMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Year;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
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
    Preconditions.checkArgument(pukItems != null, new IllegalArgumentException("Puk Items cannot be null"));
    List<PukItem> existingPukItems = getExistingPukItem(puk.getPukId(), pukItems);

    BigDecimal totalBudget = BigDecimal.ZERO;
    for (PukItem pukItem : pukItems) {
      pukItem.setTotalPrice(pukItem.getPerMeasurementPrice()
              .multiply(new BigDecimal(pukItem.getQuantity()))
              .multiply(new BigDecimal(pukItem.getTotalActivity())));
      pukItem.setPuk(puk);

      totalBudget = totalBudget.add(pukItem.getTotalPrice());
    }
    for (PukItem existingPukItem : existingPukItems) {
      totalBudget = totalBudget.add(existingPukItem.getTotalPrice());
    }


    puk.setBudget(totalBudget);
    puk.setPukYear(Year.now().getValue());
    Puk savedPuk = pukRepository.save(puk);
    CopyOnWriteArrayList<PukItem> copyOfPukItems = new CopyOnWriteArrayList<>(savedPuk.getPukItems());
    pukItemRepository.save(copyOfPukItems);

    return savedPuk;
  }

  private List<PukItem> getExistingPukItem(long pukId, Set<PukItem> currentPukItem) {
    List<PukItem> existingPukItems = pukItemRepository.findByPukId(pukId);
    Iterator<PukItem> existingPukIterators = existingPukItems.iterator();
    while (existingPukIterators.hasNext()) {
      PukItem currentPuk = existingPukIterators.next();
      for (PukItem pukItem : currentPukItem) {
        if (currentPuk.getPukItemId() == pukItem.getPukItemId()) {
          existingPukIterators.remove();
        }
      }
    }
    return existingPukItems;
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
}
