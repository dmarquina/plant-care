package com.dmarquina.plantcare.util.exceptionhandler;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public class PlantServerErrorException extends RuntimeException {
  public PlantServerErrorException(String message) {
    super(message);
  }
}