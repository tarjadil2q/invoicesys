package com.pce.service;

import com.pce.domain.PukItem;
import com.pce.repository.PukItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
@Service
public class PukItemServiceImpl implements PukItemService {

  @Autowired
  private PukItemRepository pukItemRepository;

  @Override
  public Optional<PukItem> getPukItemByPukItemId(long id) {
    return Optional.ofNullable(pukItemRepository.findOne(id));
  }

  public void calculateTotalPriceFor(PukItem pukItem) {
    pukItem.setTotalPrice(pukItem.getPerMeasurementPrice()
            .multiply(new BigDecimal(pukItem.getQuantity()))
            .multiply(new BigDecimal(pukItem.getTotalActivity())));
  }


}
