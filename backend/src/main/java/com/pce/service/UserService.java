package com.pce.service;

import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.UserCreationForm;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Collection;
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

  /**
   * Create a new user, {@link User}.
   *
   * @param form the form data to create the user
   * @return created user
   */
  User create(UserCreationForm form);

  User create(User user, Set<Role> roles);


}
