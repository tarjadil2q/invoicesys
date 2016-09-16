package com.pce.domain;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 20/08/2016.
 */
@Entity
@Table(name = "pce_approval_role", schema = "ivs")
public class PceApprovalRole {


  @Id
  @GeneratedValue(generator = "roleIdGenerator")
  @GenericGenerator(name = "roleIdGenerator", strategy = "foreign", parameters = @org.hibernate.annotations.Parameter(value = "pceApprovalRole", name = "property"))
  @Column(name = "role_id", nullable = false, updatable = false)
  private long roleId;

  @Column(name = "approval_role_sequence", unique = true)
  private int approvalRoleSequence;

  @OneToOne(cascade = CascadeType.ALL)
  @PrimaryKeyJoinColumn
  private Role pceApprovalRole;


  @Column(name = "creation_date", insertable = true, updatable = false)
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;


  public PceApprovalRole() {
  }

  public PceApprovalRole(int approvalRoleSequence, Role pceApprovalRole) {
    this.approvalRoleSequence = approvalRoleSequence;
    this.pceApprovalRole = pceApprovalRole;
  }

  public long getRoleId() {
    return roleId;
  }

  public void setRoleId(long roleId) {
    this.roleId = roleId;
  }

  public int getApprovalRoleSequence() {
    return approvalRoleSequence;
  }

  public void setApprovalRoleSequence(int approvalRoleSequence) {
    this.approvalRoleSequence = approvalRoleSequence;
  }

  public Role getPceApprovalRole() {
    return pceApprovalRole;
  }

  public void setPceApprovalRole(Role pceApprovalRole) {
    this.pceApprovalRole = pceApprovalRole;
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
}
