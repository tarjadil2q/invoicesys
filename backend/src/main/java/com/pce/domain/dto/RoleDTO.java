package com.pce.domain.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.hateoas.ResourceSupport;

import java.io.Serializable;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
public class RoleDto extends ResourceSupport implements DomainObjectDTO, Serializable{
    private long id;
    private String roleName;
    private String creationDate;
    private String updatedDate;

    public RoleDto() {
    }

    @JsonCreator
    public RoleDto(@JsonProperty("roleId") long id,
                   @JsonProperty("roleName") String roleName,
                   @JsonProperty("creationDate") String creationDate,
                   @JsonProperty("updatedDate") String updatedDate) {
        this.id = id;
        this.roleName = roleName;
        this.creationDate = creationDate;
        this.updatedDate = updatedDate;
    }

    public long getRoleId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
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
}
