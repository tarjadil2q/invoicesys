package com.pce.domain;

import com.pce.util.RoleAuthUtil;

import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 8/02/2016.
 */
public class CurrentUser extends org.springframework.security.core.userdetails.User {

    private User user;

    public CurrentUser(User user){
        super(user.getEmail(), user.getPasswordHash(), RoleAuthUtil.createRoleAuthList(user.getRoles()));
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public long getId(){
        return user.getId();
    }

    public Set<Role> getRoles(){
        return user.getRoles();
    }


}
