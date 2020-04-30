package com.dmarquina.plantcare.dto.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserEmailLoginRequest {

  @NotEmpty(message = "Es necesario el id ")
  private String id;
  private String deviceToken;

  public UserEmailLoginRequest(String id, String deviceToken) {
    this.id = id;
    this.deviceToken = deviceToken;
  }
}
