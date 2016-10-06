package com.pce.service;

import com.pce.domain.CurrentUser;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
public interface CurrentUserService {
  boolean canAccessUser(CurrentUser currentUser, Long userId);

  boolean isCurrentUserAdmin(CurrentUser currentUser);

  boolean canCurrentUserAccess(CurrentUser currentUser, Long userId);

  boolean thisRoleCanAccess(List<String> rolesToValidate, CurrentUser currentUser);

  boolean canCurrentUserApproveOrRejectPce(CurrentUser currentUser);

}
