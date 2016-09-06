package com.pce.validation.validator;

import com.pce.domain.dto.PukItemDto;
import com.pce.validation.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 6/09/2016.
 */
@Component
public class PukItemValidator implements Validator {

  @Autowired
  private PukItemMeasurementAssociationValidator pukItemMeasurementAssociationValidator;

  @Override
  public boolean supports(Class<?> clazz) {
    return PukItemDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PukItemDto pukItemDto = (PukItemDto) target;
    ValidationHelper.invokeNestedValidator(this.pukItemMeasurementAssociationValidator,
            pukItemDto, errors, "");
  }
}
