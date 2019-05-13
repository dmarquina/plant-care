package com.dmarquina.plantcare.util.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class AmazonException extends RuntimeException {
  public AmazonException(String message) {
    super(message);
  }
}