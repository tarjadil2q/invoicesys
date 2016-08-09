package com.pce.service;

import com.google.common.collect.Sets;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.domain.dto.UserCreationForm;
import com.pce.repository.RoleRepository;
import com.pce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Created by Leonardo Tarjadi on 6/02/2016.
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<User> getUserById(long id) {
        return Optional.ofNullable(userRepository.findOne(id));
    }

    @Override
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findOneByEmail(email);
    }

    @Override
    public Collection<User> getAllUsers() {
        return userRepository.findAll(new Sort("email"));
    }


    public Page<User> getAllUsers(Pageable pageRequest){
      return userRepository.findAll(pageRequest);
    }

  @Override
  public boolean isUserExists(User user) {
    Optional<User> existingUser = userRepository.findOneByEmail(user.getEmail());
    if (existingUser.isPresent()) return false;
    return true;
  }

  @Override
    public User create(UserCreationForm form) {
        String email = form.getEmail();
        String firstName = form.getFirstName();
        String lastName = form.getLastName();
        String passwordHash = bCryptPasswordEncoder.encode(form.getPassword());
        List<Role> selectedRoles = roleRepository.findAll(form.getSelectedRoleIds());
        User user = new User(firstName, lastName, email, passwordHash, Sets.newHashSet(selectedRoles));
        return userRepository.save(user);
    }



    @Override
    public User create(User user) {
        return userRepository.save(user);
    }
}
