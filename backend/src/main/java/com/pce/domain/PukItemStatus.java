package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Entity
@Table(name = "puk_item_status", schema = "ivs")
public class PukItemStatus {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "status", nullable = false)
  private String status;

  @OneToMany(mappedBy = "pukItemStatus")
  private Set<PukItem> pukItems;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PukItemStatus(long id, String status, Set<PukItem> pukItems) {
    this.id = id;
    this.status = status;
    this.pukItems = pukItems;
  }

  public long getId() {
    return id;
  }

  public String getStatus() {
    return status;
  }

  public Set<PukItem> getPukItems() {
    return pukItems;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }
}
