package com.pce.exception;

/**
 * Created by Leonardo Tarjadi on 16/12/2016.
 */
public class GoogleCredentialsException extends RuntimeException {

  public GoogleCredentialsException(String message) {
    super(message);
  }

  public GoogleCredentialsException(String message, Throwable cause) {
    super(message, cause);
  }
}
