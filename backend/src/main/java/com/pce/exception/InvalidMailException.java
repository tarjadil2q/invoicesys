package com.pce.exception;

/**
 * Created by Leonardo Tarjadi on 3/01/2017.
 */
public class InvalidMailException extends RuntimeException {

  public InvalidMailException(String message) {
    super(message);
  }

  public InvalidMailException(String message, Throwable cause) {
    super(message, cause);
  }
}
