package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
public class PceApprovalRoleDto extends ResourceSupport implements DomainObjectDTO, Serializable {

  @ReadOnlyProperty
  private long roleId;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;
  @NotNull
  @Min(value = 1, message = "{approvalRoleSequence.min.value}")
  @Max(value = 4, message = "{approvalRoleSequence.max.value}")
  private int approvalRoleSequence;

  private RoleDto approvalRole;


  public PceApprovalRoleDto() {
  }

  @JsonCreator
  public PceApprovalRoleDto(@JsonProperty("roleId") long roleId,
                            @JsonProperty("creationDate") String creationDate,
                            @JsonProperty("updatedDate") String updatedDate,
                            @JsonProperty("approvalRoleSequence") int approvalRoleSequence,
                            @JsonProperty("approvalRole") RoleDto approvalRole) {
    this.roleId = roleId;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.approvalRoleSequence = approvalRoleSequence;
    this.approvalRole = approvalRole;
  }


  public long getRoleId() {
    return roleId;
  }

  public void setRoleId(long roleId) {
    this.roleId = roleId;
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

  public int getApprovalRoleSequence() {
    return approvalRoleSequence;
  }

  public void setApprovalRoleSequence(int approvalRoleSequence) {
    this.approvalRoleSequence = approvalRoleSequence;
  }

  public RoleDto getApprovalRole() {
    return approvalRole;
  }

  public void setApprovalRole(RoleDto approvalRole) {
    this.approvalRole = approvalRole;
  }


  @Override
  public String toString() {
    return "PceApprovalRoleDto{" +
            "roleId=" + roleId +
            ", creationDate='" + creationDate + '\'' +
            ", updatedDate='" + updatedDate + '\'' +
            ", approvalRoleSequence=" + approvalRoleSequence +
            ", approvalRole=" + approvalRole +
            '}';
  }
}
