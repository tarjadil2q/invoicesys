package com.pce.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.pce.domain.CurrentUser;
import com.pce.domain.Role;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Set;

import static com.pce.constant.RoleConstant.ADMIN;

/**
 * Created by Leonardo Tarjadi on 10/02/2016.
 */
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

  @Override
  public boolean canAccessUser(CurrentUser currentUser, Long userId) {
    if (canCurrentUserAccess(currentUser, userId)) {
      return true;
    }
    Set<Role> roles = currentUser.getRoles();
    for (Role role : roles) {
      if (ADMIN.getRoleName().equals(role.getRoleName())) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean canCurrentUserAccess(CurrentUser currentUser, Long userId) {
    Preconditions.checkArgument(currentUser != null, "Current User cannot be null");
    if (currentUser.getId() == userId) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isCurrentUserAdmin(CurrentUser currentUser) {
    Preconditions.checkArgument(currentUser != null, "Current User cannot be null");
    return thisRoleCanAccess(Lists.newArrayList(ADMIN.getRoleName()), currentUser);
  }

  public boolean thisRoleCanAccess(List<String> rolesToValidate, CurrentUser currentUser) {
    Preconditions.checkArgument(currentUser != null, "Current user cannot be null");
    Preconditions.checkArgument(!CollectionUtils.isEmpty(rolesToValidate), "Roles to validate cannot be null");
    Set<Role> roles = currentUser.getRoles();
    return roles.stream().anyMatch(role -> rolesToValidate.contains(role.getRoleName()));
  }
}
