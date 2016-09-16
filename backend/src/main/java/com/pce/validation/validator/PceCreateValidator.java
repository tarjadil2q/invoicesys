package com.pce.validation.validator;

import com.pce.domain.dto.PceDto;
import com.pce.validation.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
@Component
public class PceCreateValidator implements Validator {
  @Autowired
  private PukAssociatedValidator pukAssociatedValidator;
  @Autowired
  private RecipientBankAssociatedValidator recipientBankAssociatedValidator;

  @Override
  public boolean supports(Class<?> clazz) {
    return PceDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PceDto pceDto = (PceDto) target;


    ValidationHelper.invokeNestedValidator(this.pukAssociatedValidator,
            pceDto.getAssociatedPuk(), errors, "puk");


    ValidationHelper.invokeNestedValidator(this.recipientBankAssociatedValidator,
            pceDto.getRecipientBankAccount(), errors, "recipientBankAccount");

  }
}
