package com.pce.repository;

import com.pce.domain.Pce;
import com.pce.domain.PceItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
public interface PceItemRepository extends JpaRepository<PceItem, Long> {

  @Query("FROM PceItem pi WHERE pi.pce.pceId = :pceId")
  List<PceItem> findByPceId(@Param("pceId") long pceId);

  @Query("FROM PceItem pi WHERE pi.pce.pceId = :pceId AND pi.pceItemId = :pceItemId")
  PceItem findByPceIdAndPceItemId(@Param("pceId") long pceId,
                                  @Param("pceItemId") long pceItemId);

  Page<PceItem> findByPce(Pce pce, Pageable pageRequest);


}
