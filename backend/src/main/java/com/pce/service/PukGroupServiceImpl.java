package com.pce.service;

import com.pce.domain.PukGroup;
import com.pce.repository.PukGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
@Service
public class PukGroupServiceImpl implements PukGroupService {
  @Autowired
  private PukGroupRepository pukGroupRepository;

  @Override
  public Optional<PukGroup> getPukGroupById(long id) {
    return Optional.ofNullable(pukGroupRepository.findOne(id));
  }

  @Override
  public List<PukGroup> findPukGroupByPukGroupNameIgnoreCase(String pukGroupName) {
    return pukGroupRepository.findByPukGroupNameIgnoreCase(pukGroupName);
  }

  @Override
  public PukGroup createOrUpdatePukGroup(PukGroup pukGroup) {
    return pukGroupRepository.save(pukGroup);
  }

  @Override
  public Page<PukGroup> getAllAvailablePukGroup(Pageable pageRequest) {
    return pukGroupRepository.findAll(pageRequest);
  }
}
