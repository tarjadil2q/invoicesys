package com.pce.domain.dto;

import java.io.Serializable;
import java.util.Calendar;
import java.util.StringJoiner;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
public class RoleDto implements DomainObjectDTO, Serializable{
    private long id;
    private String roleName;
    private String creationDate;
    private String updatedDate;

    public RoleDto() {
    }

    public RoleDto(long id, String roleName, String creationDate, String updatedDate) {
        this.id = id;
        this.roleName = roleName;
        this.creationDate = creationDate;
        this.updatedDate = updatedDate;
    }

    public long getId() {
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
