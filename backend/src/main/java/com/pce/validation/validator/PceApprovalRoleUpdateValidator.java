package com.pce.validation.validator;

import com.pce.domain.PceApprovalRole;
import com.pce.domain.dto.PceApprovalRoleDto;
import com.pce.service.PceApprovalRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 11/09/2016.
 */
@Component
public class PceApprovalRoleUpdateValidator implements Validator {

  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;

  @Override
  public boolean supports(Class<?> clazz) {
    return PceApprovalRoleDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PceApprovalRoleDto pceApprovalRoleDto = (PceApprovalRoleDto) target;
    long roleId = pceApprovalRoleDto.getRoleId();
    Optional<PceApprovalRole> currentPceApprovalRole = pceApprovalRoleService.findPceApprovalRoleById(roleId);
    if (!currentPceApprovalRole.isPresent()) {
      errors.rejectValue("roleId", "roleId.not.exists", "Role Id " + roleId + " not exist in the system, please select different one");
    }
  }
}
