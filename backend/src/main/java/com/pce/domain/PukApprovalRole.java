package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 18/08/2016.
 */
@Entity
@Table(name = "puk_approval_role", schema = "ivs")
public class PukApprovalRole {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  private int approvalRoleSequence;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "approval_role_id", referencedColumnName = "id", nullable = false)
  private Role role;


  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  public PukApprovalRole(int approvalRoleSequence, Role role) {
    this.approvalRoleSequence = approvalRoleSequence;
    this.role = role;
  }

  public long getId() {
    return id;
  }

  public int getApprovalRoleSequence() {
    return approvalRoleSequence;
  }

  public Role getRole() {
    return role;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }
}
