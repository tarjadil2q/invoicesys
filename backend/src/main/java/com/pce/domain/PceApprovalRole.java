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

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "approval_role_id", referencedColumnName = "id", nullable = false)
  private Role pceApprovalRole;


  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PceApprovalRole(int approvalRoleSequence, Role pceApprovalRole) {
    this.approvalRoleSequence = approvalRoleSequence;
    this.pceApprovalRole = pceApprovalRole;
  }
}
