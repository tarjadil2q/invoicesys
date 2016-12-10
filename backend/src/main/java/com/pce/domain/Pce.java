package com.pce.domain;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Arrays;
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
  private long pceId;

  @Column(name = "pce_no", nullable = false)
  private String pceNo;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "puk_id", referencedColumnName = "id", nullable = false)
  private Puk associatedPuk;

  @Column(name = "total_amount", nullable = false)
  private BigDecimal totalAmount;

  @Column(name = "total_amount_in_word", nullable = true)
  private String totalAmountInWord;

  @ManyToOne(cascade = CascadeType.REFRESH)
  @JoinColumn(name = "recipient_bank_account_id", referencedColumnName = "id", nullable = false)
  private RecipientBankAccount recipientBankAccount;

  @Column(name = "remarks", nullable = true)
  private String remarks;

  @ManyToMany(cascade = CascadeType.REFRESH)
  @JoinTable(name = "pce_approver_user", schema = "ivs", joinColumns = @JoinColumn(name = "pce_id", referencedColumnName = "id"),
          inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
  private Set<User> approvers;

  @OneToMany(mappedBy = "pce", cascade = CascadeType.REMOVE)
  private Set<PceItem> pceItems;

  @Column(name = "invoice_image")
  private byte[] invoiceImage;

  @CreationTimestamp
  @Column(name = "creation_date", insertable = true, updatable = false)
  private Calendar creationDate;

  @UpdateTimestamp
  private Calendar updatedDate;

  @Column(name = "pce_year")
  private int pceYear;


  public Pce() {
  }

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

  public long getPceId() {
    return pceId;
  }

  public void setPceId(long pceId) {
    this.pceId = pceId;
  }

  public String getPceNo() {
    return pceNo;
  }

  public void setPceNo(String pceNo) {
    this.pceNo = pceNo;
  }

  public Puk getAssociatedPuk() {
    return associatedPuk;
  }

  public void setAssociatedPuk(Puk associatedPuk) {
    this.associatedPuk = associatedPuk;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getTotalAmountInWord() {
    return totalAmountInWord;
  }

  public void setTotalAmountInWord(String totalAmountInWord) {
    this.totalAmountInWord = totalAmountInWord;
  }

  public RecipientBankAccount getRecipientBankAccount() {
    return recipientBankAccount;
  }

  public void setRecipientBankAccount(RecipientBankAccount recipientBankAccount) {
    this.recipientBankAccount = recipientBankAccount;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public Set<User> getApprovers() {
    return approvers;
  }

  public void setApprovers(Set<User> approvers) {
    this.approvers = approvers;
  }

  public Set<PceItem> getPceItems() {
    return pceItems;
  }

  public void setPceItems(Set<PceItem> pceItems) {
    this.pceItems = pceItems;
  }

  public byte[] getInvoiceImage() {
    return invoiceImage;
  }

  public void setInvoiceImage(byte[] invoiceImage) {
    this.invoiceImage = invoiceImage;
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

  public int getPceYear() {
    return pceYear;
  }

  public void setPceYear(int pceYear) {
    this.pceYear = pceYear;
  }

  @Override
  public String toString() {
    return "Pce{" +
            "pceId=" + pceId +
            ", pceNo='" + pceNo + '\'' +
            ", associatedPuk=" + associatedPuk +
            ", totalAmount=" + totalAmount +
            ", totalAmountInWord='" + totalAmountInWord + '\'' +
            ", recipientBankAccount=" + recipientBankAccount +
            ", remarks='" + remarks + '\'' +
            ", approvers=" + approvers +
            ", pceItems=" + pceItems +
            ", invoiceImage=" + Arrays.toString(invoiceImage) +
            ", creationDate=" + creationDate +
            ", updatedDate=" + updatedDate +
            ", pceYear=" + pceYear +
            '}';
  }
}
