package com.pce.domain;

import javax.persistence.*;

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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getTypeOfMeasurement() {
    return typeOfMeasurement;
  }

  public void setTypeOfMeasurement(String typeOfMeasurement) {
    this.typeOfMeasurement = typeOfMeasurement;
  }
}
