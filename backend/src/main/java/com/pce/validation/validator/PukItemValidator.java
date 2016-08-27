package com.pce.validation.validator;

import com.pce.validation.PukItemMeasurementAssoc;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Leonardo Tarjadi on 27/08/2016.
 */
public class PukItemValidator implements ConstraintValidator<PukItemMeasurementAssoc, String> {

  private boolean isUpdate;

  @Override
  public void initialize(PukItemMeasurementAssoc constraintAnnotation) {
    this.isUpdate = constraintAnnotation.isUpdate();
  }

  @Override
  public boolean isValid(String value, ConstraintValidatorContext context) {
    return false;
  }


}
