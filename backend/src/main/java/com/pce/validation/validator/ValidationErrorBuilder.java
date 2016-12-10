package com.pce.validation.validator;

import com.pce.domain.dto.ApiError;
import com.pce.domain.dto.DomainObjectDTO;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 31/08/2016.
 */
public class ValidationErrorBuilder {
  public static ResponseEntity<Resource<DomainObjectDTO>> fromBindingErrors(Errors errors) {
    ApiError error = new ApiError(HttpStatus.NOT_FOUND, "Validation failed. " + errors.getErrorCount() + " error(s)");
    List<String> errorMessages = new ArrayList<>();

    for (ObjectError objectError : errors.getAllErrors()) {
      errorMessages.add(objectError.getDefaultMessage());
    }
    error.setErrors(errorMessages);
    return new ResponseEntity(new Resource<>(error), HttpStatus.NOT_FOUND);

  }

  public static ResponseEntity<Resource<DomainObjectDTO>> fromUserCreatedError(String errorMessage) {
    ApiError error = new ApiError(HttpStatus.NOT_FOUND, "Validation failed " + errorMessage);
    return new ResponseEntity(new Resource<>(error), HttpStatus.NOT_FOUND);
  }
}
