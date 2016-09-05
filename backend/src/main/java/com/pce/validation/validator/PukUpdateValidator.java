package com.pce.validation.validator;

import com.pce.domain.Puk;
import com.pce.domain.dto.PukDto;
import com.pce.service.PukService;
import com.pce.validation.ValidationHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 5/09/2016.
 */
@Component
public class PukUpdateValidator implements Validator {

  @Autowired
  private PukService pukService;

  @Autowired
  private PukGroupAssociationValidator pukGroupAssociationValidator;

  @Autowired
  private PukItemMeasurementAssociationValidator pukItemMeasurementAssociationValidator;


  @Override
  public boolean supports(Class<?> clazz) {
    return PukDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PukDto pukDto = (PukDto) target;
    long pukId = pukDto.getPukId();
    Optional<Puk> currentPukOptional = pukService.getPukByPukId(pukId);
    if (!currentPukOptional.isPresent()) {
      errors.rejectValue("pukId", "pukId.not.exists", "Puk Id " + pukId + " not exist in the system, please select different one");
    }
    Puk pukById = currentPukOptional.get();

    String pukNo = pukDto.getPukNo();
    Optional<Puk> pukFound = pukService.getPukByPukNoIgnoreCase(pukNo);
    if (pukFound.isPresent() && !pukById.getPukNo().equals(pukFound.get().getPukNo())) {
      errors.rejectValue("pukNo", "pukNo.exists", "Puk No " + pukNo + " already exist in the system, please select different one");
    }

    ValidationHelper.invokeNestedValidator(this.pukGroupAssociationValidator,
            pukDto.getPukGroup(), errors, "pukGroup");
    ValidationHelper.invokeNestedValidatorForList(this.pukItemMeasurementAssociationValidator,
            pukDto.getPukItems(),
            errors, "pukItems");


  }
}
