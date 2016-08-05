package com.pce.service;

import com.pce.domain.User;
import com.pce.domain.dto.UserCreationForm;

import java.util.Collection;
import java.util.Optional;

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
     * @param email email to retrieve User.
     * @return User
     */
    Optional<User> getUserByEmail(String email);

    /**
     * Return All users, {@link User}.
     * @return List of all Users
     */
    Collection<User> getAllUsers();

    /**
     * Create a new user, {@link User}.
     * @param form the form data to create the user
     * @return created user
     */
    User create(UserCreationForm form);

    User create(User user);

}
