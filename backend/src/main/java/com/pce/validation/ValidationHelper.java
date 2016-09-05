package com.pce.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 5/09/2016.
 */
public class ValidationHelper {

  public static <TEntity> void invokeNestedValidator(Validator validator, TEntity entity, Errors errors, String subPath) {
    try {
      errors.pushNestedPath(subPath);
      ValidationUtils.invokeValidator(validator, entity, errors);
    } finally {
      errors.popNestedPath();
    }
  }

  public static <TEntity> void invokeNestedValidatorForList(Validator validator, List<TEntity> entities, Errors errors, String subPathRoot) {
    for (int index = 0; index < entities.size(); index++) {
      invokeNestedValidator(validator, entities.get(index), errors, subPathRoot + "[" + index + "]");
    }
  }

  private ValidationHelper() {
  }
}
