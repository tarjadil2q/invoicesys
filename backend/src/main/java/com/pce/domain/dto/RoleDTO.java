package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
@Relation(collectionRelation = "roleList")
public class RoleDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  @ReadOnlyProperty
  private long roleId;
  @NotEmpty
  private String roleName;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;

  public RoleDto() {
  }

  @JsonCreator
  public RoleDto(@JsonProperty("roleId") long roleId,
                 @JsonProperty("roleName") String roleName,
                 @JsonProperty("creationDate") String creationDate,
                 @JsonProperty("updatedDate") String updatedDate) {
    this.roleId = roleId;
    this.roleName = roleName;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
  }

  public long getRoleId() {
    return roleId;
  }

  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }

  public String getRoleName() {
    return roleName;
  }

  public void setRoleName(String roleName) {
    this.roleName = roleName;
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
}
