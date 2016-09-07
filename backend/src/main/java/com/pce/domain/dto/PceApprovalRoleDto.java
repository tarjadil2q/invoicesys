package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
public class PceApprovalRoleDto extends ResourceSupport implements DomainObjectDTO, Serializable {

  @ReadOnlyProperty
  private long pceApprovalRoleId;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;
  @NotNull
  private int approvalRoleSequence;
  @NotEmpty
  private RoleDto approvalRole;


  public PceApprovalRoleDto() {
  }

  @JsonCreator
  public PceApprovalRoleDto(@JsonProperty("pceApprovalRoleId") long pceApprovalRoleId,
                            @JsonProperty("creationDate") String creationDate,
                            @JsonProperty("updatedDate") String updatedDate,
                            @JsonProperty("approvalRoleSequence") int approvalRoleSequence,
                            @JsonProperty("approvalRole") RoleDto approvalRole) {
    this.pceApprovalRoleId = pceApprovalRoleId;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.approvalRoleSequence = approvalRoleSequence;
    this.approvalRole = approvalRole;
  }


  public long getPceApprovalRoleId() {
    return pceApprovalRoleId;
  }

  public void setPceApprovalRoleId(long pceApprovalRoleId) {
    this.pceApprovalRoleId = pceApprovalRoleId;
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
            "pceApprovalRoleId=" + pceApprovalRoleId +
            ", creationDate='" + creationDate + '\'' +
            ", updatedDate='" + updatedDate + '\'' +
            ", approvalRoleSequence=" + approvalRoleSequence +
            ", approvalRole=" + approvalRole +
            '}';
  }
}
