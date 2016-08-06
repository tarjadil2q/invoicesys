package com.pce.service;

import com.pce.domain.Role;
import com.pce.domain.dto.DomainObjectDTO;
import com.pce.domain.dto.RoleCreationForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 13/02/2016.
 */
public interface RoleService {

    Page<DomainObjectDTO> getAllAvailableRoles(Pageable pageRequest);

    List<Role> getAllAvailableRoles();


    Role create(RoleCreationForm roleCreationForm);

    List<Role> findRoleByRoleNameIgnoreCase(String roleName);

}
