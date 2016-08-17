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
  private long id;

  private String typeOfMeasurement;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PukItemMeasurement(long id, String typeOfMeasurement) {
    this.id = id;
    this.typeOfMeasurement = typeOfMeasurement;
  }

  public long getId() {
    return id;
  }

  public String getTypeOfMeasurement() {
    return typeOfMeasurement;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }
}