package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;
import org.springframework.hateoas.core.Relation;

import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 16/08/2016.
 */
@Relation(collectionRelation = "pukList")
public class PukDto extends ResourceSupport implements DomainObjectDTO, Serializable {

  private long id;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;
  @NotEmpty
  private String pukNo;
  @NotEmpty
  private String pukDescription;
  @NotEmpty
  private String budget;


  public PukDto() {
  }

  @JsonCreator
  public PukDto(@JsonProperty("pukId") long id,
                @JsonProperty("creationDate") String creationDate,
                @JsonProperty("updatedDate") String updatedDate,
                @JsonProperty("pukNo") String pukNo,
                @JsonProperty("pukDescription") String pukDescription,
                @JsonProperty("budget") String budget) {
    this.id = id;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.pukNo = pukNo;
    this.pukDescription = pukDescription;
    this.budget = budget;
  }

  public long getPukId() {
    return id;
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

  public String getBudget() {
    return budget;
  }


  public void setId(long id) {
    this.id = id;
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

  public void setBudget(String budget) {
    this.budget = budget;
  }
}
