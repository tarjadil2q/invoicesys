package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Entity
@Table(name = "puk_item_measurement", schema = "ivs")
public class PukItemMeasurement {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long pukItemMeasurementId;

  @Column(name = "type_of_measurement", nullable = false)
  private String typeOfMeasurement;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PukItemMeasurement() {
  }

  public PukItemMeasurement(long pukItemMeasurementId, String typeOfMeasurement) {
    this.pukItemMeasurementId = pukItemMeasurementId;
    this.typeOfMeasurement = typeOfMeasurement;
  }

  public long getPukItemMeasurementId() {
    return pukItemMeasurementId;
  }

  public void setPukItemMeasurementId(long pukItemMeasurementId) {
    this.pukItemMeasurementId = pukItemMeasurementId;
  }

  public String getTypeOfMeasurement() {
    return typeOfMeasurement;
  }

  public void setTypeOfMeasurement(String typeOfMeasurement) {
    this.typeOfMeasurement = typeOfMeasurement;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(Calendar creationDate) {
    this.creationDate = creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(Calendar updatedDate) {
    this.updatedDate = updatedDate;
  }
}
