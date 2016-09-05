package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.http.HttpStatus;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 10/08/2016.
 */
public class ApiError extends ResourceSupport implements DomainObjectDTO, Serializable {


  private HttpStatus status;
  private String message;
  private List<String> errors;

  @JsonCreator
  public ApiError(@JsonProperty("status") HttpStatus status,
                  @JsonProperty("errorMessage")String message) {
    this.status = status;
    this.message = message;
  }

  @JsonCreator
  public ApiError(
          @JsonProperty("status") HttpStatus status,
          @JsonProperty("errorMessage") String message,
          @JsonProperty("errors") List<String> errors) {
    this.status = status;
    this.message = message;
    this.errors = errors;
  }

  @JsonCreator
  public ApiError(@JsonProperty("status") HttpStatus status,
                  @JsonProperty("errorMessage") String message,
                  @JsonProperty("errors") String error) {
    this.status = status;
    this.message = message;
    errors = Arrays.asList(error);
  }

  public String getMessage() {
    return message;
  }

  public HttpStatus getStatus() {
    return status;
  }

  public List<String> getErrors() {
    return errors;
  }

  public void setErrors(List<String> errors) {
    this.errors = errors;
  }

  @Override
  public String toString() {
    return "ApiError{" +
            "status=" + status +
            ", message='" + message + '\'' +
            ", errors=" + errors +
            '}';
  }
}
