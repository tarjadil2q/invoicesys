package com.pce.domain.dto;

import com.pce.domain.CurrentUser;
import com.pce.domain.Pce;
import com.pce.domain.User;

import java.util.Set;

/**
 * Created by Leonardo Tarjadi on 17/09/2016.
 */
public class PceApprovalWrapperDto {

  private CurrentUser currentUser;
  private Set<User> pukGroupUsers;
  private Pce pce;


  public PceApprovalWrapperDto(CurrentUser currentUser, Set<User> pukGroupUsers, Pce pce) {
    this.currentUser = currentUser;
    this.pukGroupUsers = pukGroupUsers;
    this.pce = pce;
  }

  public CurrentUser getCurrentUser() {
    return currentUser;
  }

  public void setCurrentUser(CurrentUser currentUser) {
    this.currentUser = currentUser;
  }

  public Set<User> getPukGroupUsers() {
    return pukGroupUsers;
  }

  public void setPukGroupUsers(Set<User> pukGroupUsers) {
    this.pukGroupUsers = pukGroupUsers;
  }

  public Pce getPce() {
    return pce;
  }

  public void setPce(Pce pce) {
    this.pce = pce;
  }
}
