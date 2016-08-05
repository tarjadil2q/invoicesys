package com.pce.service;

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
        if (currentUser != null){
            if (currentUser.getId() == userId){
                return true;
            }
            Set<Role> roles = currentUser.getRoles();
            for (Role role : roles){
                if (RoleConstant.ADMIN.getRoleName().equals(role.getRoleName())){
                    return true;
                }
            }
        }
        return false;

    }
}
