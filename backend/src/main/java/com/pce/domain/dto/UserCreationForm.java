package com.pce.domain.dto;

import com.pce.domain.Role;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.LongAccumulator;

/**
 * Created by Leonardo Tarjadi on 6/02/2016.
 */
public class UserCreationForm {

    @NotEmpty
    private String email = "";
    @NotEmpty
    private String password = "";
    @NotEmpty
    private String passwordRepeated = "";

    private List<Role> roles;

    @NotNull
    private Set<Long> selectedRoleIds;

    @NotEmpty
    private String firstName = "";

    @NotEmpty
    private String lastName = "";


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPasswordRepeated() {
        return passwordRepeated;
    }

    public void setPasswordRepeated(String passwordRepeated) {
        this.passwordRepeated = passwordRepeated;
    }

    public List<Role> getRoles() {
        return roles;
    }

    public void setRoles(List<Role> roles) {
        this.roles = roles;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Long> getSelectedRoleIds() {
        return selectedRoleIds;
    }

    public void setSelectedRoleIds(Set<Long> selectedRoleIds) {
        this.selectedRoleIds = selectedRoleIds;
    }

    @Override
    public String toString() {
        return "UserCreationForm{" +
                "email='" + email + '\'' +
                ", password=***'" +  + '\'' +
                ", passwordRepeated=***'" + passwordRepeated + '\'' +
                ", roles=" + roles +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                '}';
    }
}
