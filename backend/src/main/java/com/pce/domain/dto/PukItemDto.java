package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pce.domain.PukItemMeasurement;
import com.pce.validation.PukItemMeasureAssociation;
import com.pce.validation.group.NewPuk;
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
  private long pukItemId;
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


  private BigDecimal totalPrice;

  public PukItemDto() {
  }

  @JsonCreator
  public PukItemDto(@JsonProperty("pukItemId") long pukItemId,
                    @JsonProperty("activityName") String activityName,
                    @JsonProperty("quantity") int quantity,
                    @JsonProperty("totalActivity") int totalActivity,
                    @JsonProperty("perMeasurementPrice") BigDecimal perMeasurementPrice,
                    @JsonProperty("totalPrice") BigDecimal totalPrice,
                    @JsonProperty("pukItemMeasurement") PukItemMeasurement pukItemMeasurement) {
    this.pukItemId = pukItemId;
    this.activityName = activityName;
    this.quantity = quantity;
    this.totalActivity = totalActivity;
    this.perMeasurementPrice = perMeasurementPrice;
    this.pukItemMeasurement = pukItemMeasurement;
    this.totalPrice = totalPrice;
  }


  public long getPukItemId() {
    return pukItemId;
  }

  public void setPukItemId(long pukItemId) {
    this.pukItemId = pukItemId;
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

  public String getActivityName() {
    return activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public int getTotalActivity() {
    return totalActivity;
  }

  public void setTotalActivity(int totalActivity) {
    this.totalActivity = totalActivity;
  }

  public BigDecimal getPerMeasurementPrice() {
    return perMeasurementPrice;
  }

  public void setPerMeasurementPrice(BigDecimal perMeasurementPrice) {
    this.perMeasurementPrice = perMeasurementPrice;
  }

  public PukItemMeasurement getPukItemMeasurement() {
    return pukItemMeasurement;
  }

  public void setPukItemMeasurement(PukItemMeasurement pukItemMeasurement) {
    this.pukItemMeasurement = pukItemMeasurement;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  @Override
  public String toString() {
    return "PukItemDto{" +
            "pukItemId=" + pukItemId +
            ", creationDate='" + creationDate + '\'' +
            ", updatedDate='" + updatedDate + '\'' +
            ", activityName='" + activityName + '\'' +
            ", quantity=" + quantity +
            ", totalActivity=" + totalActivity +
            ", perMeasurementPrice=" + perMeasurementPrice +
            ", pukItemMeasurement=" + pukItemMeasurement +
            ", totalPrice=" + totalPrice +
            '}';
  }
}
