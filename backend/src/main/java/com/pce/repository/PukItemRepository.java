package com.pce.repository;

import com.pce.domain.PukItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
public interface PukItemRepository extends JpaRepository<PukItem, Long> {
  @Query("FROM PukItem pi WHERE pi.puk.pukId = :pukId")
  List<PukItem> findByPukId(@Param("pukId") long pukId);

  @Query("FROM PukItem pi WHERE pi.puk.pukId = :pukId AND pi.pukItemId = :pukItemId")
  PukItem findByPukIdAndPukItemId(@Param("pukId") long pukId,
                                  @Param("pukItemId") long pukItemId);
}
