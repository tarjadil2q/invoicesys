package com.pce.validation.validator;

import com.pce.domain.PukItemMeasurement;
import com.pce.domain.dto.PukItemDto;
import com.pce.domain.dto.PukItemMeasurementDto;
import com.pce.service.PukItemMeasurementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 1/09/2016.
 */
@Component
public class PukItemMeasurementAssociationValidator implements Validator {

  @Autowired
  private PukItemMeasurementService pukItemMeasurementService;


  public PukItemMeasurementAssociationValidator() {
  }

  @Override
  public boolean supports(Class<?> clazz) {
    return PukItemDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PukItemMeasurementDto pukItemMeasurement = ((PukItemDto) target).getPukItemMeasurement();
    long pukItemMeasurementId = pukItemMeasurement.getPukItemMeasurementId();
    Optional<PukItemMeasurement> pukMeasurement = pukItemMeasurementService.getPukItemMeasurementById(pukItemMeasurementId);
    if (!pukMeasurement.isPresent()) {
      errors.rejectValue("pukItemMeasurement.pukItemMeasurementId", "pukItemMeasurement.not.exists", "Puk Item measurement Id " + pukItemMeasurementId + " does not exist in the system");
    }

  }
}
