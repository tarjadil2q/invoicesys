package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Leonardo Tarjadi on 27/08/2016.
 */
public class PukGroupForPukDto extends ResourceSupport implements DomainObjectDTO, Serializable {
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

  private List<UserDto> pukGroupUsers;

  public PukGroupForPukDto() {
  }

  public PukGroupForPukDto(@JsonProperty("pukGroupId") long id,
                           @JsonProperty("creationDate") String creationDate,
                           @JsonProperty("updatedDate") String updatedDate,
                           @JsonProperty("pukGroupName") String pukGroupName,
                           @JsonProperty("pukGroupDescription") String pukGroupDescription,
                           @JsonProperty("pukGroupUsers") List<UserDto> pukGroupUsers) {
    this.id = id;
    this.creationDate = creationDate;
    this.updatedDate = updatedDate;
    this.pukGroupName = pukGroupName;
    this.pukGroupDescription = pukGroupDescription;
    this.pukGroupUsers = pukGroupUsers;
  }

  public long getPukGroupId() {
    return id;
  }

  public void setPukGroupId(long id) {
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

  public List<UserDto> getPukGroupUsers() {
    return pukGroupUsers;
  }

  public void setPukGroupUsers(List<UserDto> pukGroupUsers) {
    this.pukGroupUsers = pukGroupUsers;
  }
}
