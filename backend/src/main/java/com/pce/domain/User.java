package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 4/02/2016.
 */
@Entity
@Table(name = "user", schema = "ivs")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "email", nullable = false, unique = true)
  private String email;

  @Column(name = "password_hash", nullable = false)
  private String passwordHash;

  @Column(name = "first_name", nullable = false)
  private String firstName;

  @Column(name = "last_name", nullable = false)
  private String lastName;


  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "user_role", schema = "ivs", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles;

  @ManyToMany(cascade = CascadeType.REFRESH)
  @JoinTable(name = "user_puk_group", schema = "ivs", joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "puk_group_id", referencedColumnName = "id"))
  private Set<PukGroup> pukGroups;

  @ManyToMany(mappedBy = "approvers")
  private Set<Pce> pcesApproved;


  public User() {
  }

  public User(String firstName, String lastName, String email, String passwordHash, Set<Role> roles,
              Set<PukGroup> pukGroups) {
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.passwordHash = passwordHash;
    this.roles = roles;
    this.pukGroups = pukGroups;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getPasswordHash() {
    return passwordHash;
  }

  public void setPasswordHash(String passwordHash) {
    this.passwordHash = passwordHash;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
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

  public Set<Role> getRoles() {
    return roles;
  }

  public void setRoles(Set<Role> roles) {
    this.roles = roles;
  }

  public Set<PukGroup> getPukGroups() {
    return pukGroups;
  }

  public Set<Pce> getPcesApproved() {
    return pcesApproved;
  }

  public void setPukGroups(Set<PukGroup> pukGroups) {
    this.pukGroups = pukGroups;
  }

  public void setPcesApproved(Set<Pce> pcesApproved) {
    this.pcesApproved = pcesApproved;
  }

  @Override
  public String toString() {
    String result = String.format("User [id=%d, firstname='%s'," +
            "lastname='%s', email='%s', password='%s']%n", id, firstName, lastName, email, passwordHash);


    if (roles != null) {
      for (Role role : roles) {
        result += String.format("Role [id=%d, roleName='%s']%n", role.getId(), role.getRoleName());
      }
    }
    return result;
  }
}
