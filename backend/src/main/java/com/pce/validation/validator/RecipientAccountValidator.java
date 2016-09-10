package com.pce.validation.validator;

import com.pce.domain.dto.RecipientBankAcctDto;
import com.pce.service.RecipientBankAcctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 9/09/2016.
 */
public class RecipientAccountValidator implements Validator {

  @Autowired
  private RecipientBankAcctService recipientBankAcctService;

  @Override
  public boolean supports(Class<?> clazz) {
    return RecipientBankAcctDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {

  }
}
