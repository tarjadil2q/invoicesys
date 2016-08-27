package com.pce.validation.validator;

import com.pce.domain.Puk;
import com.pce.service.PukService;
import com.pce.validation.PukNo;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 27/08/2016.
 */

public class PukNoValidator implements ConstraintValidator<PukNo, String> {

  @Autowired
  private PukService pukService;

  private boolean update;

  @Override
  public void initialize(PukNo constraintAnnotation) {
    update = constraintAnnotation.update();
  }

  @Override
  public boolean isValid(String pukNo, ConstraintValidatorContext context) {
    if (!update) {
      Optional<Puk> pukFound = pukService.getPukByPukNoIgnoreCase(pukNo);
      return pukFound.isPresent() ? false : true;
    }
    return true;
  }
}
