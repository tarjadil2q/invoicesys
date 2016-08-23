package com.pce.service;

import com.pce.domain.PukItemMeasurement;
import com.pce.repository.PukItemMeasurementRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 23/08/2016.
 */
@Service
public class PukItemMeasurementServiceImpl implements PukItemMeasurementService {

  private PukItemMeasurementRepository pukItemMeasurementRepository;

  @Override
  public Optional<PukItemMeasurement> getPukItemMeasurementById(long id) {
    return Optional.ofNullable(pukItemMeasurementRepository.findOne(id));
  }

  @Override
  public List<PukItemMeasurement> findPukItemMeasurementByTypeOfMeasurementIgnoreCase(String typeOfMeasurement) {
    return pukItemMeasurementRepository.findByTypeOfMeasurementIgnoreCase(typeOfMeasurement);
  }

  @Override
  public PukItemMeasurement createOrUpdatePukItemMeasurement(PukItemMeasurement pukItemMeasurement) {
    return pukItemMeasurementRepository.save(pukItemMeasurement);
  }

  @Override
  public Page<PukItemMeasurement> getAllAvailablePukItemMeasurement(Pageable pageRequest) {
    return pukItemMeasurementRepository.findAll(pageRequest);
  }
}
