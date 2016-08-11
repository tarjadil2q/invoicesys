package com.pce.repository;

import com.pce.domain.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Created by Leonardo Tarjadi on 7/02/2016.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {


  List<Role> findByRoleNameIgnoreCase(String roleName);

}
