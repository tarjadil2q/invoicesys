package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PukGroupDto extends ResourceSupport implements DomainObjectDTO, Serializable {
  private long id;
  @ReadOnlyProperty
  private String creationDate;
  @ReadOnlyProperty
  private String updatedDate;
  @NotEmpty
  private String pukGroupName;
  @NotEmpty
  @ReadOnlyProperty
  private String pukGroupDescription;
  @ReadOnlyProperty
  private Set<PukDto> puks;
  @NotEmpty
  private Set<UserDto> pukGroupUsers;


  public PukGroupDto() {
  }

  @JsonCreator
  public PukGroupDto(@JsonProperty("pukGroupId") long id,
                     @JsonProperty("creationDate") String creationDate,
                     @JsonProperty("updatedDate") String updatedDate,
                     @JsonProperty("pukGroupName") String pukGroupName,
                     @JsonProperty("pukGroupDescription") String pukGroupDescription,
                     @JsonProperty("puks") Set<PukDto> puks,
                     @JsonProperty("pukGroupUsers") Set<UserDto> pukGroupUsers) {
    this.id = id;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.pukGroupName = pukGroupName;
    this.pukGroupDescription = pukGroupDescription;
    this.puks = puks;
    this.pukGroupUsers = pukGroupUsers;
  }


  public long getPukGroupId() {
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

  public Set<PukDto> getPuks() {
    return puks;
  }

  public void setPuks(Set<PukDto> puks) {
    this.puks = puks;
  }

  public Set<UserDto> getPukGroupUsers() {
    return pukGroupUsers;
  }

  public void setPukGroupUsers(Set<UserDto> pukGroupUsers) {
    this.pukGroupUsers = pukGroupUsers;
  }


}
