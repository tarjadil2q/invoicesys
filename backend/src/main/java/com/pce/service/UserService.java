package com.pce.service;

import com.pce.domain.Pce;
import com.pce.domain.PukGroup;
import com.pce.domain.Role;
import com.pce.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 6/02/2016.
 */
public interface UserService {

  /**
   * Get {@link com.pce.domain.User} by their unique primary key ID.
   *
   * @param id The primary key to lookup for a user
   * @return User
   */
  Optional<User> getUserById(long id);

  /**
   * Get {@link User} by their email.
   *
   * @param email email to retrieve User.
   * @return User
   */
  Optional<User> getUserByEmail(String email);

  Optional<User> getUserByRole(Role role);

  /**
   * Return All users, {@link User}.
   *
   * @return List of all Users
   */
  Collection<User> getAllUsers();

  /**
   * Return {@link Page} of {@link User}.
   *
   * @param pageRequest page request
   * @return Page of user
   */
  Page<User> getAllUsers(Pageable pageRequest);

  boolean isUserExists(String email);


  User createOrUpdate(User user, Set<Role> roles);

  User createOrUpdate(User user);

  User addUserToPukGroup(User user, PukGroup pukGroup);

  Page<User> getUsersForPukGroup(Pageable pageRequest, PukGroup pukGroup);

  List<User> getApproversByPce(Pce pce);

}
