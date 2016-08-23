package com.pce.service;

import com.pce.domain.PukItemMeasurement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 23/08/2016.
 */
public interface PukItemMeasurementService {
  Optional<PukItemMeasurement> getPukItemMeasurementById(long id);

  List<PukItemMeasurement> findPukItemMeasurementByTypeOfMeasurementIgnoreCase(String pukItemMeasurement);

  PukItemMeasurement createOrUpdatePukItemMeasurement(PukItemMeasurement pukItemMeasurement);

  Page<PukItemMeasurement> getAllAvailablePukItemMeasurement(Pageable pageRequest);
}
