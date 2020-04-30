package com.dmarquina.plantcare.dto.request;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class UserVerificationCodeRequest {

  @NotEmpty(message = "Es necesario el id ")
  private String id;
  @NotEmpty(message = "Es necesario el codigo de verificacion")
  private String verificationCode;

  public UserVerificationCodeRequest(String id, String verificationCode) {
    this.id = id;
    this.verificationCode = verificationCode;
  }
}
