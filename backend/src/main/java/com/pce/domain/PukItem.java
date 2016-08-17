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
  @JoinColumn(name = "approver_id", referencedColumnName = "id")
  private User approver;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "puk_id", referencedColumnName = "id")
  private Puk puk;

  public PukItem(long id, String activityName, int totalActivity, int quantity, PukItemMeasurement pukItemMeasurement, PukItemStatus pukItemStatus, BigDecimal totalPrice, BigDecimal perMeasurementPrice, User approver, Puk puk) {
    this.id = id;
    this.activityName = activityName;
    this.totalActivity = totalActivity;
    this.quantity = quantity;
    this.pukItemMeasurement = pukItemMeasurement;
    this.pukItemStatus = pukItemStatus;
    this.totalPrice = totalPrice;
    this.perMeasurementPrice = perMeasurementPrice;
    this.approver = approver;
    this.puk = puk;
  }

  public long getId() {
    return id;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }

  public String getActivityName() {
    return activityName;
  }

  public int getTotalActivity() {
    return totalActivity;
  }

  public int getQuantity() {
    return quantity;
  }

  public PukItemMeasurement getPukItemMeasurement() {
    return pukItemMeasurement;
  }

  public PukItemStatus getPukItemStatus() {
    return pukItemStatus;
  }

  public BigDecimal getPerMeasurementPrice() {
    return perMeasurementPrice;
  }

  public BigDecimal getTotalPrice() {
    return totalPrice;
  }

  public User getApprover() {
    return approver;
  }

  public Puk getPuk() {
    return puk;
  }
}
