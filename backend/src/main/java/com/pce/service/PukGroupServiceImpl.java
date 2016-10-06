package com.pce.service;

import com.google.common.base.Preconditions;
import com.pce.domain.CurrentUser;
import com.pce.domain.PukGroup;
import com.pce.domain.User;
import com.pce.repository.PukGroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 19/08/2016.
 */
@Service
public class PukGroupServiceImpl implements PukGroupService {
  @Autowired
  private PukGroupRepository pukGroupRepository;

  @Override
  public Optional<PukGroup> getPukGroupById(long id) {
    return Optional.ofNullable(pukGroupRepository.findOne(id));
  }

  @Override
  public List<PukGroup> findPukGroupByPukGroupNameIgnoreCase(String pukGroupName) {
    return pukGroupRepository.findByPukGroupNameIgnoreCase(pukGroupName);
  }

  @Override
  public PukGroup createOrUpdatePukGroup(PukGroup pukGroup) {
    return pukGroupRepository.save(pukGroup);
  }

  @Override
  @Transactional
  public Page<PukGroup> getAllAvailablePukGroup(Pageable pageRequest) {
    return pukGroupRepository.findAll(pageRequest);
  }

  @Override
  public List<PukGroup> getAllAvailablePukGroupForCurrentUser(CurrentUser currentUser) {
    Set<User> pukGroupUsers = new HashSet<>();
    pukGroupUsers.add(currentUser.getUser());
    return pukGroupRepository.findByPukGroupUsers(pukGroupUsers);
  }

  @Override
  public Page<PukGroup> getPukGroupsByUser(Pageable pageRequest, User user) {
    Preconditions.checkArgument(user != null, "User cannot be null");
    Set<User> users = new HashSet<>();
    users.add(user);
    return pukGroupRepository.findByPukGroupUsers(users, pageRequest);
  }
}
