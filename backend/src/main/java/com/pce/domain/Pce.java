package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 20/08/2016.
 */
@Entity
@Table(name = "pce", schema = "ivs")
public class Pce {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id", nullable = false, updatable = false)
  private long id;

  @Column(name = "pce_no", nullable = false)
  private String pceNo;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "puk_id", referencedColumnName = "id", nullable = false)
  private Puk associatedPuk;

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Column(name = "total_amount_in_word", nullable = false)
  private String totalAmountInWord;

  @ManyToOne(cascade = CascadeType.ALL)
  @JoinColumn(name = "recipient_bank_account_id", referencedColumnName = "id", nullable = false)
  private RecipientBankAccount recipientBankAccount;

  @Column(name = "remarks", nullable = true)
  private String remarks;

  @ManyToMany(cascade = CascadeType.ALL)
  @JoinTable(name = "pce_approver_user", schema = "ivs", joinColumns = @JoinColumn(name = "pce_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Set<User> approvers;

  @OneToMany(mappedBy = "pce")
  private Set<PceItem> pceItems;

  @Column(name = "invoice_image")
  private byte[] invoiceImage;

  @CreationTimestamp
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @Column(name = "pce_year")
  private int pceYear;


  public Pce(String pceNo, Puk associatedPuk, RecipientBankAccount recipientBankAccount, String remarks, Set<User> approvers, Set<PceItem> pceItems, byte[] invoiceImage,
             int pceYear) {
    this.pceNo = pceNo;
    this.associatedPuk = associatedPuk;
    this.recipientBankAccount = recipientBankAccount;
    this.remarks = remarks;
    this.approvers = approvers;
    this.pceItems = pceItems;
    this.invoiceImage = invoiceImage;
    this.pceYear = pceYear;
  }

  public long getId() {
    return id;
  }

  public String getPceNo() {
    return pceNo;
  }

  public Puk getAssociatedPuk() {
    return associatedPuk;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public String getTotalAmountInWord() {
    return totalAmountInWord;
  }

  public RecipientBankAccount getRecipientBankAccount() {
    return recipientBankAccount;
  }

  public String getRemarks() {
    return remarks;
  }

  public Set<User> getApprovers() {
    return approvers;
  }

  public Set<PceItem> getPceItems() {
    return pceItems;
  }

  public byte[] getInvoiceImage() {
    return invoiceImage;
  }

  public Calendar getCreationDate() {
    return creationDate;
  }

  public Calendar getUpdatedDate() {
    return updatedDate;
  }

  public int getPceYear() {
    return pceYear;
  }
}
