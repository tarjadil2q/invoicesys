package com.pce.repository;

import com.pce.domain.Pce;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceRepository extends JpaRepository<Pce, Long> {

  @Query("FROM Pce p WHERE p.associatedPuk.pukId = :pukId order by p.creationDate desc")
  Page<Pce> findByPukId(@Param("pukId") long pukId, Pageable pageRequest);

}
