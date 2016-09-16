package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
@Relation(collectionRelation = "recipientBankAcctList")
public class RecipientBankAccountDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  @ReadOnlyProperty
  private long recipientBankAccountId;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;

  private String acctName;

  private String acctNumber;
  private String bsb;


  public RecipientBankAccountDto() {
  }

  @JsonCreator
  public RecipientBankAccountDto(@JsonProperty("recipientBankAccountId") long recipientBankAccountId,
                                 @JsonProperty("creationDate") String creationDate,
                                 @JsonProperty("updatedDate") String updatedDate,
                                 @JsonProperty("acctName") String acctName,
                                 @JsonProperty("acctNumber") String acctNumber,
                                 @JsonProperty("bsb") String bsb) {
    this.recipientBankAccountId = recipientBankAccountId;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.acctName = acctName;
    this.acctNumber = acctNumber;
    this.bsb = bsb;
  }

  public long getRecipientBankAccountId() {
    return recipientBankAccountId;
  }

  public void setRecipientBankAccountId(long recipientBankAccountId) {
    this.recipientBankAccountId = recipientBankAccountId;
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

  public String getAcctName() {
    return acctName;
  }

  public void setAcctName(String acctName) {
    this.acctName = acctName;
  }

  public String getAcctNumber() {
    return acctNumber;
  }

  public void setAcctNumber(String acctNumber) {
    this.acctNumber = acctNumber;
  }

  public String getBsb() {
    return bsb;
  }

  public void setBsb(String bsb) {
    this.bsb = bsb;
  }

  @Override
  public String toString() {
    return "RecipientBankAccountDto{" +
            "recipientBankAccountId=" + recipientBankAccountId +
            ", creationDate='" + creationDate + '\'' +
            ", updatedDate='" + updatedDate + '\'' +
            ", acctName='" + acctName + '\'' +
            ", acctNumber='" + acctNumber + '\'' +
            ", bsb='" + bsb + '\'' +
            '}';
  }
}
