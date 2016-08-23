package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.domain.Puk;
import com.pce.domain.PukItem;
import com.pce.repository.PukRepository;
import com.pce.service.mapper.PukMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

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
  private PukItemService pukItemService;

  @Override
  public Page<Puk> getAllAvailablePuk(Pageable pageRequest) {
    return pukRepository.findAll(pageRequest);
  }

  @Override
  public Puk createOrUpdatePuk(Puk puk) {
    Preconditions.checkArgument(puk != null, new IllegalArgumentException("Puk cannot be null"));
    Preconditions.checkArgument(puk.getPukGroup() != null, new IllegalArgumentException("Puk Group cannot be null"));
    Preconditions.checkArgument(puk.getPukItems() != null, new IllegalArgumentException("Puk Items cannot be null"));
    BigDecimal totalBudget = BigDecimal.ZERO;
    for (PukItem pukItem : puk.getPukItems()) {
      pukItem.setTotalPrice(pukItem.getPerMeasurementPrice()
              .multiply(new BigDecimal(pukItem.getQuantity()))
              .multiply(new BigDecimal(pukItem.getTotalActivity())));
      totalBudget.add(pukItem.getTotalPrice());
    }
    puk.setBudget(totalBudget);

    Puk savedPuk = pukRepository.save(puk);
    return savedPuk;
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
