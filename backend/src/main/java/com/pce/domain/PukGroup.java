package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.ReadOnlyProperty;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Entity
@Table(name = "puk_group", schema = "ivs")
public class PukGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long pukGroupId;

  @Column(name = "puk_group_name", nullable = false)
  private String pukGroupName;

  @Column(name = "puk_group_description", nullable = false)
  private String pukGroupDescription;

  @OneToMany(mappedBy = "pukGroup", fetch = FetchType.LAZY)
  private Set<Puk> puks;


  @ManyToMany(mappedBy = "pukGroups", fetch = FetchType.LAZY)
  private Set<User> pukGroupUsers;

  @CreationTimestamp
  @CreatedDate
  @ReadOnlyProperty
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PukGroup() {
  }

  public PukGroup(long pukGroupId, String pukGroupName, String pukGroupDescription, Set<Puk> puks,
                  Set<User> pukGroupUsers) {
    this.pukGroupId = pukGroupId;
    this.pukGroupName = pukGroupName;
    this.pukGroupDescription = pukGroupDescription;
    this.puks = puks;
    this.pukGroupUsers = pukGroupUsers;
  }

  public long getPukGroupId() {
    return pukGroupId;
  }

  public String getPukGroupName() {
    return pukGroupName;
  }

  public String getPukGroupDescription() {
    return pukGroupDescription;
  }

  public Set<Puk> getPuks() {
    return puks;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }

  public Set<User> getPukGroupUsers() {
    return pukGroupUsers;
  }

  public void setPukGroupName(String pukGroupName) {
    this.pukGroupName = pukGroupName;
  }

  @ReadOnlyProperty
  public void setPukGroupDescription(String pukGroupDescription) {
    this.pukGroupDescription = pukGroupDescription;
  }

  public void setPukGroupId(long pukGroupId) {
    this.pukGroupId = pukGroupId;
  }
}
