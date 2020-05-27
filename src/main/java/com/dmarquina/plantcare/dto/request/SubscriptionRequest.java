package com.dmarquina.plantcare.dto.request;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Valid
public class SubscriptionRequest {
  @NotEmpty(message = "Es necesario el id del dueño")
  private String userId;
  private long days;
}
