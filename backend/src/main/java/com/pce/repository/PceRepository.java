package com.pce.repository;

import com.pce.domain.Pce;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Leonardo Tarjadi on 8/09/2016.
 */
public interface PceRepository extends JpaRepository<Pce, Long> {

}
