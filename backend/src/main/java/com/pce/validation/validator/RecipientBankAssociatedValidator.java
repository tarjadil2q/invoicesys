package com.pce.validation.validator;

import com.pce.domain.dto.RecipientBankAcctDto;
import com.pce.service.RecipientBankAcctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
@Component
public class RecipientBankAssociatedValidator implements Validator {

  @Autowired
  private RecipientBankAcctService recipientBankAcctService;

  @Override
  public boolean supports(Class<?> clazz) {
    return RecipientBankAcctDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    RecipientBankAcctDto recipientBankAcctDto = (RecipientBankAcctDto) target;

    long recipientBankAcctId = recipientBankAcctDto.getRecipientBankAcctId();
    if (!recipientBankAcctService.findRecipientBankAccountById(recipientBankAcctId).isPresent()) {
      errors.rejectValue("recipientBankAcctId", "recipient.not.exists", "Recipient Bank Account  " + recipientBankAcctId + " is not exists in the system, please select different one");
    }
  }
}
