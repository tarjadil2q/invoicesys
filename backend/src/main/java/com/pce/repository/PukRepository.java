package com.pce.repository;

import com.pce.domain.Pce;
import com.pce.domain.Puk;
import com.pce.domain.PukGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
public interface PukRepository extends JpaRepository<Puk, Long> {

  Puk findByPukNoIgnoreCase(String pukNo);

  Page<Puk> findByPukGroup(PukGroup pukGroup, Pageable pageable);

  List<Puk> findByPukGroupIn(List<PukGroup> pukGroups);

  Puk findByAssociatedPces(Set<Pce> pce);
}
