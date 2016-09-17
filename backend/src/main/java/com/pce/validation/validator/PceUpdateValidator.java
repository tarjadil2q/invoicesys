package com.pce.validation.validator;

import com.pce.domain.Pce;
import com.pce.domain.dto.PceDto;
import com.pce.domain.dto.PukDto;
import com.pce.domain.dto.RecipientBankAccountDto;
import com.pce.service.PceService;
import com.pce.validation.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
@Component
public class PceUpdateValidator implements Validator {

  @Autowired
  private PceService pceService;

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
    long pceId = pceDto.getPceId();
    Optional<Pce> currentPceOptional = pceService.getPceByPceId(pceId);
    if (!currentPceOptional.isPresent()) {
      errors.rejectValue("pceId", "pceId.not.exists", "Pce Id " + pceId + " not exist in the system, please select different one");
    }
    PukDto associatedPuk = pceDto.getAssociatedPuk();
    if (associatedPuk != null) {
      ValidationHelper.invokeNestedValidator(this.pukAssociatedValidator,
              associatedPuk, errors, "puk");
    }

    RecipientBankAccountDto recipientBankAccount = pceDto.getRecipientBankAccount();
    if (recipientBankAccount != null) {
      ValidationHelper.invokeNestedValidator(this.recipientBankAssociatedValidator,
              recipientBankAccount, errors, "recipientBankAccount");
    }

    if (!CollectionUtils.isEmpty(pceDto.getPceItems())) {
      errors.rejectValue("pceItems", "pceItems.exists", "Cannot have pce items, please remove");
    }
  }
}
