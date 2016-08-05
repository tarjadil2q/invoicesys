package com.pce.validator;

import com.pce.domain.Role;
import com.pce.domain.dto.RoleCreationForm;
import com.pce.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 14/02/2016.
 */
@Component
public class RoleCreateValidator implements Validator {

    @Autowired
    private RoleService roleService;

    @Override
    public boolean supports(Class<?> clazz) {
        return clazz.equals(RoleCreationForm.class);
    }

    @Override
    public void validate(Object target, Errors errors) {
        RoleCreationForm roleCreationForm = (RoleCreationForm)target;
        validateRoleName(roleCreationForm, errors);

    }

    private void validateRoleName(RoleCreationForm roleCreationForm, Errors errors){
        List<Role> rolesByRoleName = roleService.findRoleByRoleNameIgnoreCase(roleCreationForm.getRoleName());
        if (!CollectionUtils.isEmpty(rolesByRoleName)){
            errors.reject("role.exists", "Role already exists");
        }
    }
}
