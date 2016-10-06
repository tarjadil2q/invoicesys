package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Entity
@Table(name = "puk", schema = "ivs")
public class Puk {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long pukId;

  @CreationTimestamp
  @Column(name = "creation_date", insertable = true, updatable = false)
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


  @ManyToOne(cascade = {CascadeType.REFRESH}, fetch = FetchType.LAZY)
  @JoinColumn(name = "puk_group_id", referencedColumnName = "id", nullable = false)
  private PukGroup pukGroup;

  @OneToMany(mappedBy = "puk", cascade = {CascadeType.MERGE}, fetch = FetchType.LAZY)
  private Set<PukItem> pukItems;

  @OneToMany(mappedBy = "associatedPuk", fetch = FetchType.LAZY)
  private Set<Pce> associatedPces;


  public Puk() {
  }

  public Puk(long pukId, String pukNo, String pukDescription, BigDecimal budget, int pukYear, PukGroup pukGroup, Set<PukItem> pukItems) {
    this.pukId = pukId;
    this.pukNo = pukNo;
    this.pukDescription = pukDescription;
    this.budget = budget;
    this.pukYear = pukYear;
    this.pukGroup = pukGroup;
    this.pukItems = pukItems;
  }

  public long getPukId() {
    return pukId;
  }

  public void setPukId(long pukId) {
    this.pukId = pukId;
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

  public String getPukNo() {
    return pukNo;
  }

  public void setPukNo(String pukNo) {
    this.pukNo = pukNo;
  }

  public String getPukDescription() {
    return pukDescription;
  }

  public void setPukDescription(String pukDescription) {
    this.pukDescription = pukDescription;
  }

  public BigDecimal getBudget() {
    return budget;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  public int getPukYear() {
    return pukYear;
  }

  public void setPukYear(int pukYear) {
    this.pukYear = pukYear;
  }

  public PukGroup getPukGroup() {
    return pukGroup;
  }

  public void setPukGroup(PukGroup pukGroup) {
    this.pukGroup = pukGroup;
  }

  public Set<PukItem> getPukItems() {
    return pukItems;
  }

  public void setPukItems(Set<PukItem> pukItems) {
    this.pukItems = pukItems;
  }

  public Set<Pce> getAssociatedPces() {
    return associatedPces;
  }

  public void setAssociatedPces(Set<Pce> associatedPces) {
    this.associatedPces = associatedPces;
  }
}
