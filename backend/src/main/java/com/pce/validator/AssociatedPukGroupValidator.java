package com.pce.validator;


import com.pce.domain.dto.PukGroupForPukDto;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 28/08/2016.
 */
public class AssociatedPukGroupValidator implements Validator {

  @Override
  public boolean supports(Class<?> clazz) {
    return PukGroupForPukDto.class.equals(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

  }
}
