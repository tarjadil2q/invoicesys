package com.pce.validation;

import com.pce.validation.validator.PukItemMeasurementValidator;
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
@Constraint(validatedBy = PukItemMeasurementValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface PukItemMeasureAssociation {
  String message() default "{pukItemMeasurementError}";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};


  /**
   * Defines several <code>@PukItemMeasureAssociation</code> annotations on the same element
   *
   * @author Leo
   */
  @Target({METHOD, FIELD})
  @Retention(RUNTIME)
  @Documented
  @interface List {
    PukItemMeasureAssociation[] value();
  }
}
