package com.pce.repository;

import com.pce.domain.Pce;
import com.pce.domain.PukGroup;
import com.pce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 5/02/2016.
 */
public interface UserRepository extends JpaRepository<User, Long> {

  Optional<User> findOneByEmail(String email);

  Page<User> findByPukGroups(Set<PukGroup> pukGroups, Pageable pageable);

  List<User> findByPcesApproved(Set<Pce> pces);
}
