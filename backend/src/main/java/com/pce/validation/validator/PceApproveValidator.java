package com.pce.validation.validator;

import com.pce.domain.*;
import com.pce.domain.dto.PceApprovalWrapperDto;
import com.pce.service.PceApprovalRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Tarjadi on 17/09/2016.
 */
@Component
public class PceApproveValidator implements Validator {

  @Autowired
  private PceApprovalRoleService pceApprovalRoleService;


  @Override
  public boolean supports(Class<?> clazz) {
    return PceApprovalWrapperDto.class.isAssignableFrom(clazz);
  }

  @Override
  public void validate(Object target, Errors errors) {
    PceApprovalWrapperDto pceApproval = (PceApprovalWrapperDto) target;
    CurrentUser principal = pceApproval.getCurrentUser();

    Pce pce = pceApproval.getPce();
    Set<User> pukGroupUsers = pce.getAssociatedPuk().getPukGroup().getPukGroupUsers();

    User currentUser = principal.getUser();

    boolean userMatched = pukGroupUsers.stream().anyMatch(user -> user.getId() == currentUser.getId());
    if (!userMatched) {
      errors.rejectValue("currentUser", "currentUser.not.matched", "Current user not in the puk group user and cannot approve the pce");
    }

    Set<Role> roles = principal.getRoles();
    List<PceApprovalRole> validApprovalRoles = pceApprovalRoleService.findAllAvailableApprovalRoleOrderBySequenceNoAsc();

    List<Role> listOfValidRole = validApprovalRoles.stream().map(pceRole -> pceRole.getPceApprovalRole()).collect(Collectors.toList());

    List<Role> validApprovalRole = roles.stream().filter(role -> listOfValidRole.contains(role)).collect(Collectors.toList());
    if (CollectionUtils.isEmpty(validApprovalRole)) {
      errors.rejectValue("currentUser", "currentUser.not.valid.roles", "Current user does not have a valid approval role");
    }

  }
}
