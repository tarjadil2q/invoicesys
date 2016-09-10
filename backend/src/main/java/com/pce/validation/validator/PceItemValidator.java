package com.pce.validation.validator;

import com.pce.domain.PceItem;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
public class PceItemValidator implements Validator {
  @Override
  public boolean supports(Class<?> clazz) {
    return PceItem.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    
  }
}
