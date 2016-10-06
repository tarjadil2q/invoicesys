package com.pce.service;

import com.pce.domain.Pce;
import com.pce.domain.Puk;
import com.pce.domain.PukGroup;
import com.pce.domain.PukItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
public interface PukService {
  Page<Puk> getAllAvailablePuk(Pageable pageRequest);

  Puk createOrUpdatePuk(Puk puk);

  Optional<Puk> getPukByPukNoIgnoreCase(String pukNo);

  boolean isPukExist(long id);

  Optional<Puk> getPukByPukId(long id);

  PukItem createOrUpdatePukItem(Puk puk
          , PukItem pukItem);

  public Optional<PukItem> getPukItemByPukItemId(long id);

  Optional<PukItem> getPukItemByPukIdAndPukItemId(long pukId, long pukItemId);

  Page<Puk> getPuksForPukGroup(PukGroup pukGroup, Pageable pageRequest);

  Optional<Puk> getPukByPce(Pce pce);

  Page<PukItem> getPukItemsByPukId(long pukId, Pageable pageRequest);

}
