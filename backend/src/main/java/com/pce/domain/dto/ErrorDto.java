package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 10/08/2016.
 */
public class ErrorDto extends ResourceSupport implements DomainObjectDTO, Serializable {


  private String message;

  @JsonCreator
  public ErrorDto(@JsonProperty("errorMessage") String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  @Override
  public String toString() {
    return "ErrorDto{" +
            "message='" + message + '\'' +
            '}';
  }
}
