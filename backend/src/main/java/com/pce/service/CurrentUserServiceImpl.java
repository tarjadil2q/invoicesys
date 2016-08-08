package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.constant.RoleConstant;
import com.pce.domain.CurrentUser;
import com.pce.domain.Role;
import org.springframework.stereotype.Service;

import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 10/02/2016.
 */
@Service
public class CurrentUserServiceImpl implements CurrentUserService {

    @Override
    public boolean canAccessUser(CurrentUser currentUser, Long userId) {
        Preconditions.checkArgument(currentUser != null, "Current User cannot be null");
        if (currentUser.getId() == userId){
            return true;
        }
        Set<Role> roles = currentUser.getRoles();
        for (Role role : roles){
            if (RoleConstant.ADMIN.getRoleName().equals(role.getRoleName())){
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean isCurrentUserAdmin(CurrentUser currentUser) {
        Preconditions.checkArgument(currentUser != null, "Current User cannot be null");
        return thisRoleCanAccess(RoleConstant.ADMIN, currentUser);
    }

    private boolean thisRoleCanAccess(RoleConstant role, CurrentUser currentUser){
        Set<Role> roles = currentUser.getRoles();
        for (Role currentUserRole : roles){
            if (role.getRoleName().equals(currentUserRole.getRoleName())){
                return true;
            }
        }
        return false;
    }
}
