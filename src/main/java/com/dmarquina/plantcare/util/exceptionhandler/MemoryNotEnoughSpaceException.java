package com.dmarquina.plantcare.util.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class MemoryNotEnoughSpaceException extends RuntimeException {
  public MemoryNotEnoughSpaceException(String message) {
    super(message);
  }
}