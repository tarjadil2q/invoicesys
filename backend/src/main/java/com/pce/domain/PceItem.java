package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 20/08/2016.
 */
@Entity
@Table(name = "pce_item", schema = "ivs")
public class PceItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long pceItemId;

  @Column(name = "pce_item_description", nullable = false)
  private String pceItemDescription;

  @Column(name = "price_amount", nullable = false)
  private BigDecimal priceAmount;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "pce_id", referencedColumnName = "id")
  private Pce pce;

  @CreationTimestamp
  @Column(name = "creation_date", insertable = true, updatable = false)
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;


  public PceItem() {
  }

  public PceItem(String pceItemDescription, BigDecimal priceAmount, Pce pce) {
    this.pceItemDescription = pceItemDescription;
    this.priceAmount = priceAmount;
    this.pce = pce;
  }

  public long getPceItemId() {
    return pceItemId;
  }

  public void setPceItemId(long pceItemId) {
    this.pceItemId = pceItemId;
  }

  public String getPceItemDescription() {
    return pceItemDescription;
  }

  public void setPceItemDescription(String pceItemDescription) {
    this.pceItemDescription = pceItemDescription;
  }

  public BigDecimal getPriceAmount() {
    return priceAmount;
  }

  public void setPriceAmount(BigDecimal priceAmount) {
    this.priceAmount = priceAmount;
  }

  public Pce getPce() {
    return pce;
  }

  public void setPce(Pce pce) {
    this.pce = pce;
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
