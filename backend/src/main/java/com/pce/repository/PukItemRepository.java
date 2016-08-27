package com.pce.repository;

import com.pce.domain.PukItem;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
public interface PukItemRepository extends JpaRepository<PukItem, Long> {
}
