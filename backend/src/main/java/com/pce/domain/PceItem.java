package com.pce.domain;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * Created by Leonardo Tarjadi on 20/08/2016.
 */
@Entity
@Table(name = "pce_item", schema = "ivs")
public class PceItem {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "pce_item_description", nullable = false)
  private String pceItemDescription;

  @Column(name = "price_amount", nullable = false)
  private BigDecimal priceAmount;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "pce_id", referencedColumnName = "id")
  private Pce pce;

  public PceItem(String pceItemDescription, BigDecimal priceAmount, Pce pce) {
    this.pceItemDescription = pceItemDescription;
    this.priceAmount = priceAmount;
    this.pce = pce;
  }

  public long getId() {
    return id;
  }

  public String getPceItemDescription() {
    return pceItemDescription;
  }

  public BigDecimal getPriceAmount() {
    return priceAmount;
  }

  public Pce getPce() {
    return pce;
  }
}
