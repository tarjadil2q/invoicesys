package com.pce.domain.dto;

import org.hibernate.validator.constraints.NotBlank;

/**
 * Created by Leonardo Tarjadi on 14/02/2016.
 */
public class RoleCreationForm {

    @NotBlank
    private String roleName = "";


    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public String toString() {
        return "RoleCreationForm{" +
                "roleName='" + roleName + '\'' +
                '}';
    }
}
