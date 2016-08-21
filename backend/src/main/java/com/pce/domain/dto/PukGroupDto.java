package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.pce.domain.Puk;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
public class PukGroupDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  @ReadOnlyProperty
  private long id;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;
  @NotEmpty
  private String pukGroupName;
  @NotEmpty
  private String pukGroupDescription;
  @ReadOnlyProperty
  @NotEmpty
  private Set<Puk> puks;


  public PukGroupDto() {
  }

  @JsonCreator
  public PukGroupDto(@JsonProperty("pukGroupId") long id,
                     @JsonProperty("creationDate") String creationDate,
                     @JsonProperty("updatedDate") String updatedDate,
                     @JsonProperty("pukGroupName") String pukGroupName,
                     @JsonProperty("pukGroupDescription") String pukGroupDescription,
                     @JsonProperty("puks") Set<Puk> puks) {
    this.id = id;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.pukGroupName = pukGroupName;
    this.pukGroupDescription = pukGroupDescription;
    this.puks = puks;
  }


  public long pukGroupId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
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

  public String getPukGroupName() {
    return pukGroupName;
  }

  public void setPukGroupName(String pukGroupName) {
    this.pukGroupName = pukGroupName;
  }

  public String getPukGroupDescription() {
    return pukGroupDescription;
  }

  public void setPukGroupDescription(String pukGroupDescription) {
    this.pukGroupDescription = pukGroupDescription;
  }

  public Set<Puk> getPuks() {
    return puks;
  }

  public void setPuks(Set<Puk> puks) {
    this.puks = puks;
  }

}
