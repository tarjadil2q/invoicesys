package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 20/08/2016.
 */
@Entity
@Table(name = "recipient_bank_account", schema = "ivs",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"acct_number", "bsb"})})
public class RecipientBankAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long recipientBankAccountId;

  @Column(name = "bsb", nullable = false)
  private String bsb;

  @Column(name = "acct_name", nullable = false, unique = true)
  private String acctName;

  @Column(name = "acct_number", nullable = false, unique = true)
  private String acctNumber;

  @CreationTimestamp
  @Column(name = "creation_date", insertable = true, updatable = false)
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @OneToMany(mappedBy = "recipientBankAccount")
  private Set<Pce> associatedPces;

  public RecipientBankAccount() {
  }

  public RecipientBankAccount(long recipientBankAccountId, String bsb, String acctName, String acctNumber, Set<Pce> associatedPces) {
    this.recipientBankAccountId = recipientBankAccountId;
    this.bsb = bsb;
    this.acctName = acctName;
    this.acctNumber = acctNumber;
    this.associatedPces = associatedPces;
  }

  public long getRecipientBankAccountId() {
    return recipientBankAccountId;
  }

  public String getBsb() {
    return bsb;
  }

  public String getAcctName() {
    return acctName;
  }

  public String getAcctNumber() {
    return acctNumber;
  }

  public Set<Pce> getAssociatedPces() {
    return associatedPces;
  }

  public void setRecipientBankAccountId(long recipientBankAccountId) {
    this.recipientBankAccountId = recipientBankAccountId;
  }

  public void setBsb(String bsb) {
    this.bsb = bsb;
  }

  public void setAcctName(String acctName) {
    this.acctName = acctName;
  }

  public void setAcctNumber(String acctNumber) {
    this.acctNumber = acctNumber;
  }

  public void setCreationDate(Calendar creationDate) {
    this.creationDate = creationDate;
  }

  public void setUpdatedDate(Calendar updatedDate) {
    this.updatedDate = updatedDate;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }

  public void setAssociatedPces(Set<Pce> associatedPces) {
    this.associatedPces = associatedPces;
  }
}
