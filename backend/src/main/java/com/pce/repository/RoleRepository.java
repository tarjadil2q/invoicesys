package com.pce.repository;

import com.pce.domain.Role;
import com.pce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 7/02/2016.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {


  List<Role> findByRoleNameIgnoreCase(String roleName);

  Page<Role> findByUsers(Set<User> users, Pageable pageRequest);

}
