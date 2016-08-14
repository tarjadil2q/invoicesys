package com.pce.service;

import com.pce.domain.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 13/02/2016.
 */
public interface RoleService {

  Page<Role> getAllAvailableRoles(Pageable pageRequest);

  List<Role> getAllAvailableRoles();

  public Role createOrUpdateRole(Role role);


  List<Role> findRoleByRoleNameIgnoreCase(String roleName);

  boolean isRoleExist(long roleId);

  public Optional<Role> getRoleById(long id);

}
