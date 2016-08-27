package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 23/08/2016.
 */
public class PukItemMeasurementDto extends ResourceSupport implements DomainObjectDTO, Serializable {

  @ReadOnlyProperty
  private long pukItemMeasurementId;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;

  @NotEmpty
  private String typeOfMeasurement;

  public PukItemMeasurementDto() {
  }

  @JsonCreator
  public PukItemMeasurementDto(@JsonProperty("pukItemMeasurementId") long pukItemMeasurementId,
                               @JsonProperty("typeOfMeasurement") String typeOfMeasurement) {
    this.pukItemMeasurementId = pukItemMeasurementId;
    this.typeOfMeasurement = typeOfMeasurement;
  }


  public long getPukItemMeasurementId() {
    return pukItemMeasurementId;
  }

  public void setPukItemMeasurementId(long pukItemMeasurementId) {
    this.pukItemMeasurementId = pukItemMeasurementId;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

  public String getTypeOfMeasurement() {
    return typeOfMeasurement;
  }

  public void setTypeOfMeasurement(String typeOfMeasurement) {
    this.typeOfMeasurement = typeOfMeasurement;
  }
}
