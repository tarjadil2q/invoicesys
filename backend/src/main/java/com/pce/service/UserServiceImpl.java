package com.pce.service;

import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.pce.domain.Pce;
import com.pce.domain.PukGroup;
import com.pce.domain.Role;
import com.pce.domain.User;
import com.pce.repository.RoleRepository;
import com.pce.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.*;

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


  public Page<User> getAllUsers(Pageable pageRequest) {
    return userRepository.findAll(pageRequest);
  }

  @Override
  public boolean isUserExists(String email) {
    Optional<User> existingUser = userRepository.findOneByEmail(email);
    if (existingUser.isPresent()) return true;
    return false;
  }

  /*@Override
  public User createOrUpdate(UserCreationForm form) {
    String email = form.getEmail();
    String firstName = form.getFirstName();
    String lastName = form.getLastName();
    String passwordHash = bCryptPasswordEncoder.encode(form.getPassword());
    List<Role> selectedRoles = roleRepository.findAll(form.getSelectedRoleIds());
    User user = new User(firstName, lastName, email, passwordHash, Sets.newHashSet(selectedRoles));
    return userRepository.save(user);
  }*/


  @Override
  public User createOrUpdate(User user, Set<Role> roles) {
    user.setRoles(roles);
    return userRepository.save(user);
  }

  @Override
  public User createOrUpdate(User user) {
    return userRepository.save(user);
  }

  @Override
  public User addUserToPukGroup(User user, PukGroup pukGroup) {
    Set<PukGroup> existingUserPukGroups = user.getPukGroups();
    Set<PukGroup> pukGroups = Sets.newHashSet();
    if (!CollectionUtils.isEmpty(existingUserPukGroups)) {
      pukGroups.addAll(existingUserPukGroups);
    }
    pukGroups.add(pukGroup);
    user.setPukGroups(pukGroups);
    return userRepository.save(user);
  }

  @Transactional
  @Override
  public Page<User> getUsersForPukGroup(Pageable pageRequest, PukGroup pukGroup) {
    Preconditions.checkArgument(pukGroup != null, "Puk Group cannot be null");
    Set<PukGroup> pukGroups = new HashSet<>();
    pukGroups.add(pukGroup);
    return userRepository.findByPukGroups(pukGroups, pageRequest);
  }

  @Override
  public List<User> getApproversByPce(Pce pce) {
    Preconditions.checkArgument(pce != null, "Pce  cannot be null");
    Set<Pce> pces = new HashSet<>();
    pces.add(pce);
    return userRepository.findByPcesApproved(pces);
  }

  @Override
  public Optional<User> getUserByRole(Role role) {
    return Optional.ofNullable(userRepository.findByRolesIn(Lists.newArrayList(role)));
  }
}
