package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.repository.RoleRepository;
import com.pce.service.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 13/02/2016.
 */
@Service
public class RoleServiceImpl implements RoleService {

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private RoleMapper roleMapper;

  @Override
  public Page<Role> getAllAvailableRoles(Pageable pageRequest) {
    return roleRepository.findAll(pageRequest);
  }


  public Role createOrUpdateRole(Role role) {
    return roleRepository.save(role);
  }

  @Override
  public List<Role> findRoleByRoleNameIgnoreCase(String roleName) {
    List<Role> rolesByRoleName = roleRepository.findByRoleNameIgnoreCase(roleName);
    return rolesByRoleName;

  }

  @Override
  public List<Role> getAllAvailableRoles() {
    return roleRepository.findAll();
  }

  @Override
  public boolean isRoleExist(long roleId) {
    return roleRepository.findOne(roleId) != null;
  }

  @Override
  public Optional<Role> getRoleById(long id) {
    return Optional.ofNullable(roleRepository.findOne(id));
  }

  @Override
  public Page<Role> getRolesForUser(Pageable pageRequest, User user) {
    Preconditions.checkArgument(user != null, "User cannot be null");
    Set<User> users = new HashSet<>();
    users.add(user);
    return roleRepository.findByUsers(users, pageRequest);
  }
}
