package com.pce.service;

import com.pce.domain.PukGroup;
import com.pce.repository.PukGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
@Service
public class PukGroupServiceImpl implements PukGroupService {
  @Autowired
  private PukGroupRepository pukGroupRepository;

  /*@Override
  public Optional<PukGroup> getPukByPukId(long id) {
    return Optional.ofNullable(pukGroupRepository.findOne(id));
  }*/

  @Override
  public Optional<PukGroup> getPukGroupById(long id) {
    return null;
  }
}
