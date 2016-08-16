package com.pce.repository;

import com.pce.domain.Puk;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
public interface PukRepository extends JpaRepository<Puk, Long> {

  Puk findByPukNoIgnoreCase(String pukNo);
}
