package com.cognifide.aem.stubs.core;

public class StubsException extends RuntimeException {

  public StubsException(String message) {
    super(message);
  }

  public StubsException(String message, Throwable throwable) {
    super(message, throwable);
  }
}
