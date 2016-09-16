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
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
@Component
public class RecipientAccountUpdateValidator implements Validator {

  @Autowired
  private RecipientBankAcctService recipientBankAcctService;

  @Override
  public boolean supports(Class<?> clazz) {
    return RecipientBankAccountDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    RecipientBankAccountDto recipientBankAccountDto = (RecipientBankAccountDto) target;

    long recipientBankAcctId = recipientBankAccountDto.getRecipientBankAccountId();
    Optional<RecipientBankAccount> recipientBankAccountOptional = recipientBankAcctService.findRecipientBankAccountById(recipientBankAcctId);
    if (!recipientBankAccountOptional.isPresent()) {
      errors.rejectValue("recipientBankAcctId", "recipientBankAcctId.not.exists", "Recipient bank account id" + recipientBankAcctId + " not exist in the system, please select different one");
    }
    RecipientBankAccount recipientBankAccount = recipientBankAccountOptional.get();


    String acctNumber = recipientBankAccountDto.getAcctNumber();
    String bsb = recipientBankAccountDto.getBsb();

    Optional<RecipientBankAccount> recipientBankAccountByAccountNumberAndBsb = recipientBankAcctService.findRecipientBankAccountByAccountNumberAndBsb(acctNumber,
            bsb);

    if (recipientBankAccountByAccountNumberAndBsb.isPresent() &&
            !recipientBankAccount.getAcctNumber().equals(recipientBankAccountByAccountNumberAndBsb.get().getAcctNumber())
            && !recipientBankAccount.getBsb().equals(recipientBankAccountByAccountNumberAndBsb.get().getBsb())) {
      errors.rejectValue("acctNumber", "acctNumber.exists", "Recipient Bank Account Number " + acctNumber + " already exist in the system, please select different one");
      errors.rejectValue("bsb", "bsb.exists", "Recipient Bank Account bsb number " + bsb + " already exist in the system, please select different one");
    }

  }
}
