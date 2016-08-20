package com.pce.domain;

import javax.persistence.*;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 20/08/2016.
 */
@Entity
@Table(name = "recipient_bank_account", schema = "ivs")
public class RecipientBankAccount {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "bsb", nullable = false)
  private String bsb;

  @Column(name = "acct_name", nullable = false, unique = true)
  private String acctName;

  @Column(name = "acct_number", nullable = false, unique = true)
  private String acctNumber;


  @OneToMany(mappedBy = "recipientBankAccount")
  private Set<Pce> associatedPces;

  public RecipientBankAccount(String bsb, String acctName, String acctNumber, Set<Pce> associatedPces) {
    this.bsb = bsb;
    this.acctName = acctName;
    this.acctNumber = acctNumber;
    this.associatedPces = associatedPces;
  }

  public long getId() {
    return id;
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
}
