package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
@Relation(collectionRelation = "pceList")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PceDto extends ResourceSupport implements DomainObjectDTO, Serializable {

  @ReadOnlyProperty
  private long pceId;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;

  private String pceNo;

  private String remarks;


  private BigDecimal totalAmount;

  private String totalAmountInWords;

  private PukDto associatedPuk;

  private RecipientBankAccountDto recipientBankAccount;

  private List<PceItemDto> pceItems;

  @ReadOnlyProperty
  private int pceYear;

  private List<UserDto> approvers;

  public PceDto() {
  }

  @JsonCreator
  public PceDto(@JsonProperty("pceId") long pceId,
                @JsonProperty("creationDate") String creationDate,
                @JsonProperty("updatedDate") String updatedDate,
                @JsonProperty("pceNo") String pceNo,
                @JsonProperty("remarks") String remarks,
                @JsonProperty("totalAmount") BigDecimal totalAmount,
                @JsonProperty("totalAmountInWords") String totalAmountInWords,
                @JsonProperty("associatedPuk") PukDto associatedPuk,
                @JsonProperty("recipientBankAccount") RecipientBankAccountDto recipientBankAccount,
                @JsonProperty("pceItems") List<PceItemDto> pceItems) {
    this.associatedPuk = associatedPuk;
    this.pceId = pceId;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.pceNo = pceNo;
    this.remarks = remarks;
    this.totalAmount = totalAmount;
    this.totalAmountInWords = totalAmountInWords;
    this.recipientBankAccount = recipientBankAccount;
    this.pceItems = pceItems;
  }


  public long getPceId() {
    return pceId;
  }

  public void setPceId(long pceId) {
    this.pceId = pceId;
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

  public String getPceNo() {
    return pceNo;
  }

  public void setPceNo(String pceNo) {
    this.pceNo = pceNo;
  }

  public String getRemarks() {
    return remarks;
  }

  public void setRemarks(String remarks) {
    this.remarks = remarks;
  }

  public BigDecimal getTotalAmount() {
    return totalAmount;
  }

  public void setTotalAmount(BigDecimal totalAmount) {
    this.totalAmount = totalAmount;
  }

  public String getTotalAmountInWords() {
    return totalAmountInWords;
  }

  public void setTotalAmountInWords(String totalAmountInWords) {
    this.totalAmountInWords = totalAmountInWords;
  }

  public PukDto getAssociatedPuk() {
    return associatedPuk;
  }

  public void setAssociatedPuk(PukDto associatedPuk) {
    this.associatedPuk = associatedPuk;
  }

  public RecipientBankAccountDto getRecipientBankAccount() {
    return recipientBankAccount;
  }

  public void setRecipientBankAccount(RecipientBankAccountDto recipientBankAccount) {
    this.recipientBankAccount = recipientBankAccount;
  }

  public List<PceItemDto> getPceItems() {
    return pceItems;
  }

  public void setPceItems(List<PceItemDto> pceItems) {
    this.pceItems = pceItems;
  }

  public int getPceYear() {
    return pceYear;
  }

  public void setPceYear(int pceYear) {
    this.pceYear = pceYear;
  }

  public List<UserDto> getApprovers() {
    return approvers;
  }

  public void setApprovers(List<UserDto> approvers) {
    this.approvers = approvers;
  }
}
