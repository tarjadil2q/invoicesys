package com.pce.repository;

import com.pce.domain.Role;
import com.pce.domain.User;
import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.repository.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 7/02/2016.
 */
public interface RoleRepository extends JpaRepository<Role, Long> {


    List<Role> findByRoleNameIgnoreCase(String roleName);
}
