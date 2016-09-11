package com.pce.validation.validator;

import com.pce.domain.dto.RoleDto;
import com.pce.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

/**
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
public class PceRoleAssociationValidator implements Validator {

  @Autowired
  private RoleService roleService;

  @Override
  public boolean supports(Class<?> clazz) {
    return RoleDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    RoleDto roleDto = (RoleDto) target;
    long roleId = roleDto.getRoleId();
    if (!roleService.getRoleById(roleId).isPresent()) {
      errors.rejectValue("roleId", "role.not.exists", "Role with role id of  " + roleId + " is not exists in the system, please select different one");
    }

  }
}
