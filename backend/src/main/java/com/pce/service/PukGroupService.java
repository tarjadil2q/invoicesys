package com.pce.service;

import com.pce.domain.PukGroup;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
public interface PukGroupService {

  Optional<PukGroup> getPukGroupById(long id);
}
