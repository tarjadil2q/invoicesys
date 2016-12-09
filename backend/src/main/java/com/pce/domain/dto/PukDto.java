package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pce.domain.Pce;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Relation(collectionRelation = "pukList")
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PukDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  @ReadOnlyProperty
  private long pukId;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;
  @NotEmpty(message = "Puk No Cannot Be Null / Empty")
  private String pukNo;
  @NotEmpty
  private String pukDescription;

  private BigDecimal budget;

  @ReadOnlyProperty
  private int pukYear;

  private PukGroupForPukDto pukGroup;

  private List<PukItemDto> pukItems;

  private List<Pce> associatedPces;


  public PukDto() {
  }

  @JsonCreator
  public PukDto(@JsonProperty("pukId") long pukId,
                @JsonProperty("creationDate") String creationDate,
                @JsonProperty("updatedDate") String updatedDate,
                @JsonProperty("pukNo") String pukNo,
                @JsonProperty("pukDescription") String pukDescription,
                @JsonProperty("budget") BigDecimal budget,
                @JsonProperty("pukGroup") PukGroupForPukDto pukGroup,
                @JsonProperty("pukItems") List<PukItemDto> pukItems,
                @JsonProperty("associatedPces") List<Pce> associatedPces) {
    this.pukId = pukId;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.pukNo = pukNo;
    this.pukDescription = pukDescription;
    this.budget = budget;
    this.pukGroup = pukGroup;
    this.pukItems = pukItems;
    this.associatedPces = associatedPces;
  }

  public long getPukId() {
    return pukId;
  }

  public String getCreationDate() {
    return creationDate;
  }

  public String getUpdatedDate() {
    return updatedDate;
  }

  public String getPukNo() {
    return pukNo;
  }

  public String getPukDescription() {
    return pukDescription;
  }

  public BigDecimal getBudget() {
    return budget;
  }


  public void setPukId(long pukId) {
    this.pukId = pukId;
  }

  public void setCreationDate(String creationDate) {
    this.creationDate = creationDate;
  }

  public void setUpdatedDate(String updatedDate) {
    this.updatedDate = updatedDate;
  }

  public void setPukNo(String pukNo) {
    this.pukNo = pukNo;
  }

  public void setPukDescription(String pukDescription) {
    this.pukDescription = pukDescription;
  }

  public void setBudget(BigDecimal budget) {
    this.budget = budget;
  }

  public int getPukYear() {
    return pukYear;
  }

  public void setPukYear(int pukYear) {
    this.pukYear = pukYear;
  }

  public PukGroupForPukDto getPukGroup() {
    return pukGroup;
  }

  public void setPukGroup(PukGroupForPukDto pukGroup) {
    this.pukGroup = pukGroup;
  }

  public List<PukItemDto> getPukItems() {
    return pukItems;
  }

  public void setPukItems(List<PukItemDto> pukItems) {
    this.pukItems = pukItems;
  }

  public List<Pce> getAssociatedPces() {
    return associatedPces;
  }

  public void setAssociatedPces(List<Pce> associatedPces) {
    this.associatedPces = associatedPces;
  }

  @Override
  public String toString() {
    return "PukDto{" +
            "pukId=" + pukId +
            ", creationDate='" + creationDate + '\'' +
            ", updatedDate='" + updatedDate + '\'' +
            ", pukNo='" + pukNo + '\'' +
            ", pukDescription='" + pukDescription + '\'' +
            ", budget=" + budget +
            ", pukYear=" + pukYear +
            ", pukGroup=" + pukGroup +
            ", pukItems=" + pukItems +
            ", associatedPces=" + associatedPces +
            '}';
  }
}
