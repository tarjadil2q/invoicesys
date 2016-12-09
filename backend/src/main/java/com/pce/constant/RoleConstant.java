package com.pce.constant;

/**
 * Created by Leonardo Tarjadi on 6/02/2016.
 */
public enum RoleConstant {
  USER("User"), ADMIN("Admin"), COMITTEE_HEAD("Comittee Head");
  private String roleName;

  RoleConstant(String roleName) {
    this.roleName = roleName;
  }

  public String getRoleName() {
    return roleName;
  }
}
