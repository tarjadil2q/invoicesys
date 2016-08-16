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
@Table(name = "puk", schema = "ivs")
public class Puk {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @Column(name = "puk_no", nullable = false, unique = true)
  private String pukNo;

  @Column(name = "puk_description", nullable = false)
  private String pukDescription;

  @Column(name = "budget", nullable = false)
  private BigDecimal budget;

  @Column(name = "puk_year", nullable = false)
  private int pukYear;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "role_id", referencedColumnName = "id", nullable = false)
  private Role role;


  public Puk(String pukNo, String pukDescription, BigDecimal budget, Role role, int pukYear) {
    this.pukNo = pukNo;
    this.pukDescription = pukDescription;
    this.budget = budget;
    this.role = role;
    this.pukYear = pukYear;
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

  public String getPukNo() {
    return pukNo;
  }

  public String getPukDescription() {
    return pukDescription;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public Role getRole() {
    return role;
  }

  public int getPukYear() {
    return pukYear;
  }
}
