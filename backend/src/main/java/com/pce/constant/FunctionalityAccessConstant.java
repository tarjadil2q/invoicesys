package com.pce.constant;

/**
 * Created by Leonardo Tarjadi on 26/08/2016.
 */
public enum FunctionalityAccessConstant {
  PUK("PUK");

  private String accessName;

  FunctionalityAccessConstant(String accessName) {
    this.accessName = accessName;
  }

  public String getAccessName() {
    return accessName;
  }
}
