package com.pce.service;

import com.pce.domain.Pce;
import com.pce.repository.PceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public class PceServiceImpl implements PceService {

  @Autowired
  private PceRepository pceRepository;

  public Page<Pce> getAllAvailablePce(Pageable pageRequest) {
    return pceRepository.findAll(pageRequest);
  }

  public Pce createOrUpdatePce(Pce pce) {
    return null;
  }

  @Override
  public Optional<Pce> getPceByPceId(long id) {
    return Optional.ofNullable(pceRepository.findOne(id));
  }
}
