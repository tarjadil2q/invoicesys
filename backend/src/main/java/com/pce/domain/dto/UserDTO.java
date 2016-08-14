package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pce.controller.UserController;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;

import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
@Relation(collectionRelation = "userList")
public class UserDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  private long id;
  @NotEmpty
  private String firstName;
  @NotEmpty
  private String lastName;

  private String email;
  private String creationDate;
  private String updatedDate;
  private List<RoleDto> roles;
  private String password;

  @NotNull
  public UserDto() {
  }

  public void setLink(long id, String rel) {
    this.add(ControllerLinkBuilder.linkTo(UserController.class).slash(id).withRel(rel).withSelfRel());
  }

  @JsonCreator
  @NotNull
  public UserDto(@JsonProperty("userId") long id,
                 @JsonProperty("firstName") String firstName,
                 @JsonProperty("lastName") String lastName,
                 @JsonProperty("email") String email,
                 @JsonProperty("creationDate") String creationDate,
                 @JsonProperty("updatedDate") String updatedDate,
                 @JsonProperty("roles") List<RoleDto> roles,
                 @JsonProperty(access = JsonProperty.Access.WRITE_ONLY, value = "password") String password) {
    this.id = id;
    this.firstName = firstName;
    this.lastName = lastName;
    this.email = email;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.roles = roles;
    this.password = password;
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

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public String toString() {
    return "UserDto{" +
            "id=" + id +
            ", firstName='" + firstName + '\'' +
            ", lastName='" + lastName + '\'' +
            ", email='" + email + '\'' +
            ", creationDate='" + creationDate + '\'' +
            ", updatedDate='" + updatedDate + '\'' +
            ", roles=" + roles +
            '}';
  }
}
