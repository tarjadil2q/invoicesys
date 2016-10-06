package com.pce.validation.validator;

import com.pce.domain.CurrentUser;
import com.pce.domain.Pce;
import com.pce.domain.User;
import com.pce.domain.dto.PceApprovalWrapperDto;
import com.pce.service.PceApprovalRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
    PceApprovalWrapperDto pceApprovalWrapper = (PceApprovalWrapperDto) target;
    CurrentUser principal = pceApprovalWrapper.getCurrentUser();


    User currentUser = principal.getUser();

    if (!isUserValidAgainstPukGroup(pceApprovalWrapper)) {
      errors.rejectValue("currentUser", "currentUser.not.matched", "Current user " + currentUser.getFirstName() + " " + currentUser.getLastName() + " not in the puk group user and cannot approve  pce");
    }

    Pce pce = pceApprovalWrapper.getPce();

    List<Long> existingApprovers = pce.getApprovers().stream().map(user -> user.getId()).collect(Collectors.toList());

    if (existingApprovers.contains(currentUser.getId())) {
      errors.rejectValue("currentUser", "currentUser.already.approve", "Current user " + currentUser.getFirstName() + " " + currentUser.getLastName() +
              " already approving pce " + pce.getPceNo());
    }

  }

  protected boolean isUserValidAgainstPukGroup(PceApprovalWrapperDto pceApprovalWrapper) {
    Pce pce = pceApprovalWrapper.getPce();
    Set<User> pukGroupUsers = pce.getAssociatedPuk().getPukGroup().getPukGroupUsers();

    User user = pceApprovalWrapper.getCurrentUser().getUser();

    boolean userMatched = pukGroupUsers.stream().anyMatch(pukGroupUser -> pukGroupUser.getId() == user.getId());
    return userMatched ? true : false;
  }
}
