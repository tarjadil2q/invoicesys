package com.pce.repository;

import com.pce.domain.PukGroup;
import com.pce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
public interface PukGroupRepository extends JpaRepository<PukGroup, Long> {

  List<PukGroup> findByPukGroupNameIgnoreCase(String pukGroupName);

  List<PukGroup> findByPukGroupUsers(Set<User> pukGroupUsers);

  Page<PukGroup> findByPukGroupUsers(Set<User> user, Pageable pageRequest);

}
