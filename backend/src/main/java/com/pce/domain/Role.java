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
@Table(name = "role", schema = "ivs")
public class Role {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "role_name", nullable = false)
  private String roleName;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @ManyToMany(mappedBy = "roles")
  private Set<User> users;

  @OneToMany(mappedBy = "role")
  private Set<Puk> puks;

  public Role() {
  }

  public Role(String roleName) {
    this.roleName = roleName;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String role) {
    this.roleName = role;
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

  public Set<User> getUsers() {
    return users;
  }

  public void setUsers(Set<User> users) {
    this.users = users;
  }

  public Set<Puk> getPuks() {
    return puks;
  }

  public void setPuks(Set<Puk> puks) {
    this.puks = puks;
  }

  @Override
  public String toString() {
    return "Role{" +
            "id=" + id +
            ", roleName='" + roleName + '\'' +
            ", creationDate=" + creationDate +
            ", updatedDate=" + updatedDate +
            ", users=" + users +
            ", puks=" + puks +
            '}';
  }
}
