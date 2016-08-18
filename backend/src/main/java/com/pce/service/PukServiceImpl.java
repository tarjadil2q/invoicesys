package com.pce.service;

import com.pce.domain.Puk;
import com.pce.repository.PukRepository;
import com.pce.service.mapper.PukMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

  @Override
  public Page<Puk> getAllAvailablePuk(Pageable pageRequest) {
    return pukRepository.findAll(pageRequest);
  }

  @Override
  public Puk createOrUpdatePuk(Puk puk) {
    return pukRepository.save(puk);
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
