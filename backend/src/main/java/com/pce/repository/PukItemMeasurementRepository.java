package com.pce.repository;

import com.pce.domain.PukItemMeasurement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 23/08/2016.
 */
public interface PukItemMeasurementRepository extends JpaRepository<PukItemMeasurement, Long> {

  List<PukItemMeasurement> findByTypeOfMeasurementIgnoreCase(String pukGroupName);

  List<PukItemMeasurement> findByPukItemMeasurementIdIn(List<Long> pukItemMeasurementId);
}
