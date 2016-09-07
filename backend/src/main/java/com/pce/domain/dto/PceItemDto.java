package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pce.domain.Pce;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Created by Leonardo Tarjadi on 7/09/2016.
 */
@Relation(collectionRelation = "pceItemList")
public class PceItemDto extends ResourceSupport implements DomainObjectDTO, Serializable {

  @ReadOnlyProperty
  private long pceItemId;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;

  @NotEmpty
  private String pceItemDescription;

  @NotNull
  private BigDecimal priceAmount;

  private Pce pce;


  public PceItemDto() {
  }

  @JsonCreator
  public PceItemDto(@JsonProperty("pceItemId") long pceItemId,
                    @JsonProperty("creationDate") String creationDate,
                    @JsonProperty("updatedDate") String updatedDate,
                    @JsonProperty("pceItemDescription") String pceItemDescription,
                    @JsonProperty("priceAmount") BigDecimal priceAmount,
                    @JsonProperty("pce") Pce pce) {
    this.pceItemId = pceItemId;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.pceItemDescription = pceItemDescription;
    this.priceAmount = priceAmount;
    this.pce = pce;
  }

  public long getPceItemId() {
    return pceItemId;
  }

  public void setPceItemId(long pceItemId) {
    this.pceItemId = pceItemId;
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

  public String getPceItemDescription() {
    return pceItemDescription;
  }

  public void setPceItemDescription(String pceItemDescription) {
    this.pceItemDescription = pceItemDescription;
  }

  public BigDecimal getPriceAmount() {
    return priceAmount;
  }

  public void setPriceAmount(BigDecimal priceAmount) {
    this.priceAmount = priceAmount;
  }

  public Pce getPce() {
    return pce;
  }

  public void setPce(Pce pce) {
    this.pce = pce;
  }
}
