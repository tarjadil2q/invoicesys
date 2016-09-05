package com.pce.validator;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 28/08/2016.
 */
public class AssociatedPukItemMeasurementValidator implements Validator {


  @Override
  public boolean supports(Class<?> clazz) {
    return false;
  }

  @Override
  public void validate(Object target, Errors errors) {
    //errors.reject();
  }
}
