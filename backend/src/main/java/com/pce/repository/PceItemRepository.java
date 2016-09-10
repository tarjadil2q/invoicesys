package com.pce.repository;

import com.pce.domain.PceItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
public interface PceItemRepository extends JpaRepository<PceItem, Long> {
}
