package com.dmarquina.plantcare.util.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
public class CustomException extends RuntimeException {
  public CustomException(String message) {
    super(message);
  }
}