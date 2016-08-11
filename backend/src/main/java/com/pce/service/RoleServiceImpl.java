package com.pce.service;

import com.pce.domain.Role;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RoleCreationForm;
import com.pce.repository.RoleRepository;
import com.pce.service.mapper.RoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
  public Page<DomainObjectDTO> getAllAvailableRoles(Pageable pageRequest) {
    Page<Role> pageRoleEntity = roleRepository.findAll(pageRequest);
    return null;
        /*return roleMapper.mapEntityPageIntoDTOPage(pageRequest, pageRoleEntity);*/
  }

  @Override
  public Role create(RoleCreationForm roleCreationForm) {
    Role newRole = new Role();
    newRole.setRoleName(roleCreationForm.getRoleName());
    return roleRepository.save(newRole);
  }

  public Role createRole(Role role) {
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
}
