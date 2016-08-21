package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pce.domain.Puk;
import com.pce.domain.PukItemMeasurement;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Leonardo Tarjadi on 21/08/2016.
 */
@Relation(collectionRelation = "pukItemList")
public class PukItemDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  @ReadOnlyProperty
  private long id;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;
  @NotEmpty
  private String activityName;
  @NotEmpty
  private int quantity;
  @NotEmpty
  private int totalActivity;
  @NotEmpty
  private BigDecimal perMeasurementPrice;
  @NotEmpty
  @ReadOnlyProperty
  private PukItemMeasurement pukItemMeasurement;

  @NotEmpty
  @ReadOnlyProperty
  private Puk associatedPuk;

  public PukItemDto() {
  }

  @JsonCreator
  public PukItemDto(@JsonProperty("pukItemId") long id,
                    @JsonProperty("activityName") String activityName,
                    @JsonProperty("quantity") int quantity,
                    @JsonProperty("totalActivity") int totalActivity,
                    @JsonProperty("perMeasurementPrice") BigDecimal perMeasurementPrice,
                    @JsonProperty("pukItemMeasurement") PukItemMeasurement pukItemMeasurement,
                    @JsonProperty("associatedPuk") Puk associatedPuk) {
    this.id = id;
    this.activityName = activityName;
    this.quantity = quantity;
    this.totalActivity = totalActivity;
    this.perMeasurementPrice = perMeasurementPrice;
    this.pukItemMeasurement = pukItemMeasurement;
    this.associatedPuk = associatedPuk;
  }


  public long getPukItemId() {
    return id;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public String getActivityName() {
    return activityName;
  }

  public int getQuantity() {
    return quantity;
  }

  public int getTotalActivity() {
    return totalActivity;
  }

  public BigDecimal getPerMeasurementPrice() {
    return perMeasurementPrice;
  }

  public PukItemMeasurement getPukItemMeasurement() {
    return pukItemMeasurement;
  }

  public Puk getAssociatedPuk() {
    return associatedPuk;
  }
}
