package com.pce.domain;

import javax.persistence.*;
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

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }
}
