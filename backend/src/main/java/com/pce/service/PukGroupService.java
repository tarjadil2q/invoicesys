package com.pce.service;

import com.pce.domain.PukGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
public interface PukGroupService {

  Optional<PukGroup> getPukGroupById(long id);

  List<PukGroup> findPukGroupByPukGroupNameIgnoreCase(String pukGroupName);

  PukGroup createOrUpdatePukGroup(PukGroup pukGroup);

  Page<PukGroup> getAllAvailablePukGroup(Pageable pageRequest);
}
