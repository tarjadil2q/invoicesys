package com.pce.validation.validator;

import com.pce.domain.PukGroup;
import com.pce.domain.dto.PukGroupForPukDto;
import com.pce.service.PukGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 27/08/2016.
 */
@Component
public class PukGroupAssociationValidator implements org.springframework.validation.Validator {

  @Autowired
  private PukGroupService pukGroupService;

  @Override
  public boolean supports(Class<?> clazz) {
    return PukGroupForPukDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PukGroupForPukDto pukGroupDto = (PukGroupForPukDto) target;
    long pukGroupId = pukGroupDto.getPukGroupId();
    Optional<PukGroup> pukGroupById = pukGroupService.getPukGroupById(pukGroupId);
    if (!pukGroupById.isPresent()) {
      errors.rejectValue("pukGroupId", "pukGroup.not.exists", "Puk Group " + pukGroupId + " is not exists in the system, please select different one");
    }
  }
}
