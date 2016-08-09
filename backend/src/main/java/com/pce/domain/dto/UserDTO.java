package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
@Relation(collectionRelation = "userList")
public class UserDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  private long id;
  private String firstName;
  private String lastName;
  private String email;
  private String creationDate;
  private String updatedDate;
  private List<RoleDto> roles;

  public UserDto() {
  }

  @JsonCreator
  public UserDto(@JsonProperty("userId")long id,
                 @JsonProperty("firstName") String firstName,
                 @JsonProperty("lastName") String lastName,
                 @JsonProperty("email") String email,
                 @JsonProperty("creationDate") String creationDate,
                 @JsonProperty("updatedDate") String updatedDate,
                 @JsonProperty("roles") List<RoleDto> roles) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.roles = roles;
  }

  public long getUserId() {
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
