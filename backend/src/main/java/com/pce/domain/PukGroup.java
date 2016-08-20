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
@Table(name = "puk_group", schema = "ivs")
public class PukGroup {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "puk_group_name", nullable = false)
  private String pukGroupName;

  @Column(name = "puk_group_description", nullable = false)
  private String pukGroupDescription;

  @OneToMany(mappedBy = "pukGroup")
  private Set<Puk> puks;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "puk_group_role_head_id", referencedColumnName = "id")
  private Role pukGroupRoleHead;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "puk_group_user", schema = "ivs", joinColumns = @JoinColumn(name = "puk_group_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Set<User> usersInGroup;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PukGroup(long id, String pukGroupName, String pukGroupDescription, Set<Puk> puks, Role pukGroupRoleHead,
                  Set<User> usersInGroup) {
    this.id = id;
    this.pukGroupName = pukGroupName;
    this.pukGroupDescription = pukGroupDescription;
    this.puks = puks;
    this.pukGroupRoleHead = pukGroupRoleHead;
    this.usersInGroup = usersInGroup;
  }

  public long getId() {
    return id;
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

  public Role getPukGroupRoleHead() {
    return pukGroupRoleHead;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }

  public Set<User> getUsersInGroup() {
    return usersInGroup;
  }
}