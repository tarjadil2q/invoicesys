package com.pce.domain.dto;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
public class UserDto implements DomainObjectDTO, Serializable {
  private long id;
  private String firstName;
  private String lastName;
  private String email;
  private String creationDate;
  private String updatedDate;
  private List<RoleDto> roles;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

  public List<RoleDto> getRoles() {
    return roles;
  }

  public void setRoles(List<RoleDto> roles) {
    this.roles = roles;
  }

  @Override
  public String toString() {
    return "UserDto{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", creationDate=" + creationDate +
            ", updatedDate=" + updatedDate +
            ", roles=" + roles +
            '}';
  }
}
