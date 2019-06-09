package com.dmarquina.plantcare.dto.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserRequest {

  @NotEmpty(message = "Es necesario el id ")
  private String id;
  @NotEmpty(message = "Es necesario el token del dispositivo")
  private String deviceToken;
  private String email;
}
