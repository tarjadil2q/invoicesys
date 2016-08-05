package com.pce.util;

import com.google.common.base.Preconditions;
import com.pce.domain.Role;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 10/02/2016.
 */
public class RoleAuthUtil {

    public static List<GrantedAuthority> createRoleAuthList(Set<Role> roles){
        Preconditions.checkArgument(roles != null);
        List<GrantedAuthority> grantedAuthorityRole = new ArrayList<GrantedAuthority>(roles.size());
        for (Role role : roles){
            String roleName = role.getRoleName();
            grantedAuthorityRole.add(new SimpleGrantedAuthority(roleName));
        }
        return grantedAuthorityRole;
    }
}
