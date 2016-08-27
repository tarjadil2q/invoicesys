package com.pce.validation.validator;

import com.pce.domain.dto.PukGroupForPukDto;
import com.pce.service.PukGroupService;
import com.pce.validation.PukGroupAssociation;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * Created by Leonardo Tarjadi on 27/08/2016.
 */
public class PukGroupAssociationValidator implements ConstraintValidator<PukGroupAssociation, PukGroupForPukDto> {

  @Autowired
  private PukGroupService pukGroupService;

  @Override
  public void initialize(PukGroupAssociation constraintAnnotation) {

  }

  @Override
  public boolean isValid(PukGroupForPukDto pukGroup, ConstraintValidatorContext context) {
    return pukGroupService.getPukGroupById(pukGroup.getPukGroupId()).isPresent() ? true : false;
  }
}
