package com.pce.repository;

import com.pce.domain.PukGroup;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
public interface PukGroupRepository extends JpaRepository<PukGroup, Long> {

  List<PukGroup> findByPukGroupNameIgnoreCase(String pukGroupName);

}
