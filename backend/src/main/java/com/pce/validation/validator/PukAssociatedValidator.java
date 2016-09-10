package com.pce.validation.validator;

import com.pce.domain.Puk;
import com.pce.domain.dto.PukDto;
import com.pce.service.PukService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
public class PukAssociatedValidator implements Validator {

  @Autowired
  private PukService pukService;

  @Override
  public boolean supports(Class<?> clazz) {
    return PukDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PukDto pukDto = (PukDto) target;
    long pukId = pukDto.getPukId();
    Optional<Puk> pukByPukId = pukService.getPukByPukId(pukId);
    if (!pukByPukId.isPresent()) {
      errors.rejectValue("pukId", "puk.not.exists", "Puk  " + pukId + " is not exists in the system, please select different one");
    }
  }
}
