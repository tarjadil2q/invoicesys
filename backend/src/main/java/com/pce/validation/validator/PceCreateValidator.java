package com.pce.validation.validator;

import com.pce.domain.dto.PceDto;
import com.pce.domain.dto.PukDto;
import com.pce.domain.dto.RecipientBankAccountDto;
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
    PukDto associatedPuk = pceDto.getAssociatedPuk();
    if (associatedPuk == null) {
      errors.rejectValue("associatedPuk", "associatedPuk.not.exists", "Associated puk not specify, please specify one");
    }
    ValidationHelper.invokeNestedValidator(this.pukAssociatedValidator,
            associatedPuk, errors, "puk");

    RecipientBankAccountDto recipientBankAccount = pceDto.getRecipientBankAccount();
    if (recipientBankAccount == null) {
      errors.rejectValue("recipientBankAccount", "recipientBank.not.exists", "Recipient bank not specify, please specify one");
    }
    ValidationHelper.invokeNestedValidator(this.recipientBankAssociatedValidator,
            recipientBankAccount, errors, "recipientBankAccount");

  }
}
