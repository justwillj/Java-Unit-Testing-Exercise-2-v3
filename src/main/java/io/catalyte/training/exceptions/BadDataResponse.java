package io.catalyte.training.exceptions;

public class BadDataResponse extends RuntimeException {

  public BadDataResponse() {
  }

  public BadDataResponse(String message) {
    super(message);
  }
}
