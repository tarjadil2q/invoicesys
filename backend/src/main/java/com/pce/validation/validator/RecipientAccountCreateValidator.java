package com.pce.validation.validator;

import com.pce.domain.RecipientBankAccount;
import com.pce.domain.dto.RecipientBankAccountDto;
import com.pce.service.RecipientBankAcctService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 9/09/2016.
 */
@Component
public class RecipientAccountCreateValidator implements Validator {

  @Autowired
  private RecipientBankAcctService recipientBankAcctService;

  @Override
  public boolean supports(Class<?> clazz) {
    return RecipientBankAccountDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    RecipientBankAccountDto recipientBankAccountDto = (RecipientBankAccountDto) target;
    String acctNumber = recipientBankAccountDto.getAcctNumber();
    String bsb = recipientBankAccountDto.getBsb();
    Optional<RecipientBankAccount> recipientBankAccountByAccountNumberAndBsb = recipientBankAcctService.findRecipientBankAccountByAccountNumberAndBsb(acctNumber,
            bsb);
    if (recipientBankAccountByAccountNumberAndBsb.isPresent()) {
      errors.rejectValue("acctNumber", "acctNumber.exists", "Recipient Bank Account Number  " + acctNumber + " is exists in the system, please select different one");
      errors.rejectValue("bsb", "bsb.exists", "Recipient Bank Account bsb number  " + bsb + " is exists in the system, please select different one");
    }
  }
}
