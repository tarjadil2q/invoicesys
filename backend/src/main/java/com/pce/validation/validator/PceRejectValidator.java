package com.pce.validation.validator;

import com.pce.domain.CurrentUser;
import com.pce.domain.Pce;
import com.pce.domain.User;
import com.pce.domain.dto.PceApprovalWrapperDto;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Leonardo Tarjadi on 23/09/2016.
 */
@Component
public class PceRejectValidator extends PceApproveValidator implements Validator {

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
      errors.rejectValue("currentUser", "currentUser.not.matched", "Current user " + currentUser.getFirstName() + " " + currentUser.getLastName() + " not in the puk group user and cannot reject  pce");
    }

    Pce pce = pceApprovalWrapper.getPce();

    List<Long> existingApprovers = pce.getApprovers().stream().map(user -> user.getId()).collect(Collectors.toList());

    if (CollectionUtils.isEmpty(existingApprovers)) {
      errors.rejectValue("pce", "pce.has.no.approver", "Current PCE " + pce.getPceNo() + " is still in draft.");
    }
  }
}
