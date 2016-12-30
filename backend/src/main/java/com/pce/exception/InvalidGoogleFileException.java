package com.pce.exception;

/**
 * Created by Leonardo Tarjadi on 29/12/2016.
 */
public class InvalidGoogleFileException extends RuntimeException {

  public InvalidGoogleFileException(String message) {
    super(message);
  }

  public InvalidGoogleFileException(String message, Throwable cause) {
    super(message, cause);
  }
}
