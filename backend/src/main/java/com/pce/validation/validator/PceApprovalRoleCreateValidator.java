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
 * Created by Leonardo Tarjadi on 10/09/2016.
 */
@Component
public class PceApprovalRoleCreateValidator implements Validator {

  @Autowired
  private PceRoleAssociationValidator pceRoleAssociationValidator;

  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;


  @Override
  public boolean supports(Class<?> clazz) {
    return PceApprovalRoleDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PceApprovalRoleDto pceApprovalRoleDto = (PceApprovalRoleDto) target;
    int approvalRoleSequence = pceApprovalRoleDto.getApprovalRoleSequence();
    Optional<PceApprovalRole> pceApprovalRoleByApprovalRoleSequence =
            pceApprovalRoleService.findPceApprovalRoleByApprovalRoleSequenceAndId(pceApprovalRoleDto.getRoleId(),
                    approvalRoleSequence);
    if (pceApprovalRoleByApprovalRoleSequence.isPresent()) {
      errors.rejectValue("approvalRoleSequence", "role.sequence.exists", "Role sequence " + approvalRoleSequence + " with role of   "
              + pceApprovalRoleByApprovalRoleSequence.get().getPceApprovalRole().getRoleName() + " is  exists in the system, please select different one");
    }
    /*ValidationHelper.invokeNestedValidator(this.pceRoleAssociationValidator,
            pceApprovalRoleDto.getApprovalRole(), errors, "approvalRole");*/
  }
}
