package com.pce.domain.dto;

import java.io.Serializable;
import java.util.Calendar;

/**
 * Created by Leonardo Tarjadi on 5/03/2016.
 */
public class RoleDTO implements DomainObjectDTO, Serializable{
    private long id;
    private String roleName;
    private Calendar creationDate;
    private Calendar updatedDate;

    public RoleDTO(long id, String roleName, Calendar creationDate, Calendar updatedDate) {
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

    public Calendar getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Calendar creationDate) {
        this.creationDate = creationDate;
    }

    public Calendar getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(Calendar updatedDate) {
        this.updatedDate = updatedDate;
    }
}
