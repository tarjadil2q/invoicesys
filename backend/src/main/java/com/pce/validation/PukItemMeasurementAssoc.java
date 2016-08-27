package com.pce.validation;

import com.pce.validation.validator.PukItemValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by Leonardo Tarjadi on 27/08/2016.
 */
@Documented
@Constraint(validatedBy = PukItemValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PukItemMeasurementAssoc {

  String message() default "{PukItemError}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};

  boolean isUpdate() default false;

  /**
   * Defines several <code>@PukItemMeasurementAssoc</code> annotations on the same element
   *
   * @author Leo
   */
  @Target({METHOD, FIELD})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    PukItemMeasurementAssoc[] value();
  }
}
