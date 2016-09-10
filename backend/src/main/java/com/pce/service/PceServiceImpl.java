package com.pce.service;

import com.pce.domain.Pce;
import com.pce.domain.PceItem;
import com.pce.repository.PceItemRepository;
import com.pce.repository.PceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

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
    return null;
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
    return null;
  }

  @Override
  public Optional<PceItem> getPceItemByPceIdAndPceItemId(long pceId, long PceItemId) {
    return null;
  }
}
