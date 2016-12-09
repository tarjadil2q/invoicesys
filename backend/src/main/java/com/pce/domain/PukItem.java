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
  private long pukItemId;


  @CreationTimestamp
  @Column(name = "creation_date", insertable = true, updatable = false)
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @Column(name = "activity_name", nullable = false)
  private String activityName;
  @Column(name = "total_activity", nullable = false)
  private int totalActivity;
  @Column(name = "quantity", nullable = false)
  private int quantity;
  @ManyToOne(cascade = CascadeType.REFRESH, fetch = FetchType.LAZY)
  @JoinColumn(name = "puk_item_measurement_id", referencedColumnName = "id", nullable = false)
  private PukItemMeasurement pukItemMeasurement;

  @Column(name = "per_measurement_price", nullable = false)
  private BigDecimal perMeasurementPrice;
  @Column(name = "total_price", nullable = false)
  private BigDecimal totalPrice;


  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "puk_id", referencedColumnName = "id")
  private Puk puk;


  public PukItem() {
  }

  public PukItem(long pukItemId, String activityName, int totalActivity, int quantity, PukItemMeasurement pukItemMeasurement, BigDecimal totalPrice, BigDecimal perMeasurementPrice, User approver, Puk puk) {
    this.pukItemId = pukItemId;
    this.activityName = activityName;
    this.totalActivity = totalActivity;
    this.quantity = quantity;
    this.pukItemMeasurement = pukItemMeasurement;
    this.totalPrice = totalPrice;
    this.perMeasurementPrice = perMeasurementPrice;
    this.puk = puk;
  }

  public long getPukItemId() {
    return pukItemId;
  }

  public void setPukItemId(long pukItemId) {
    this.pukItemId = pukItemId;
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

  public Puk getPuk() {
    return puk;
  }

  public void setPuk(Puk puk) {
    this.puk = puk;
  }
}
