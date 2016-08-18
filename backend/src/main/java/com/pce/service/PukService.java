package com.pce.service;

import com.pce.domain.Puk;
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
}
