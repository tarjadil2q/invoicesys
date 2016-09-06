package com.pce.service;

import com.pce.domain.PukItem;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
public interface PukItemService {
  public Optional<PukItem> getPukItemByPukItemId(long id);

  void calculateTotalPriceFor(PukItem pukItem);

}
