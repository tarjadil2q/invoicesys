package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Entity
@Table(name = "puk_item", schema = "ivs")
public class PukItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;


  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @Column(name = "activity_name", nullable = false)
  private String activityName;
  @Column(name = "total_activity", nullable = false)
  private int totalActivity;
  @Column(name = "quantity", nullable = false)
  private int quantity;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "puk_item_measurement_id", referencedColumnName = "id", nullable = false)
  private PukItemMeasurement pukItemMeasurement;
  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "puk_item_status_id", referencedColumnName = "id", nullable = false)
  private PukItemStatus pukItemStatus;

  @Column(name = "per_measurement_price", nullable = false)
  private BigDecimal perMeasurementPrice;
  @Column(name = "total_price", nullable = false)
  private BigDecimal totalPrice;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "user_id", referencedColumnName = "id")
  private User approver;

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public String getActivityName() {
    return activityName;
  }

  public void setActivityName(String activityName) {
    this.activityName = activityName;
  }

  public int getTotalActivity() {
    return totalActivity;
  }

  public void setTotalActivity(int totalActivity) {
    this.totalActivity = totalActivity;
  }

  public int getQuantity() {
    return quantity;
  }

  public void setQuantity(int quantity) {
    this.quantity = quantity;
  }

  public PukItemMeasurement getPukItemMeasurement() {
    return pukItemMeasurement;
  }

  public void setPukItemMeasurement(PukItemMeasurement pukItemMeasurement) {
    this.pukItemMeasurement = pukItemMeasurement;
  }

  public BigDecimal getPerMeasurementPrice() {
    return perMeasurementPrice;
  }

  public void setPerMeasurementPrice(BigDecimal perMeasurementPrice) {
    this.perMeasurementPrice = perMeasurementPrice;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public void setTotalPrice(BigDecimal totalPrice) {
    this.totalPrice = totalPrice;
  }

  public PukItemStatus getPukItemStatus() {
    return pukItemStatus;
  }

  public void setPukItemStatus(PukItemStatus pukItemStatus) {
    this.pukItemStatus = pukItemStatus;
  }

  public User getApprover() {
    return approver;
  }

  public void setApprover(User approver) {
    this.approver = approver;
  }
}
