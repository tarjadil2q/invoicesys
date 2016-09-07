package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
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
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  private int approvalRoleSequence;

  @OneToOne
  private Role pceApprovalRole;


  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PceApprovalRole(int approvalRoleSequence, Role pceApprovalRole) {
    this.approvalRoleSequence = approvalRoleSequence;
    this.pceApprovalRole = pceApprovalRole;
  }

  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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
