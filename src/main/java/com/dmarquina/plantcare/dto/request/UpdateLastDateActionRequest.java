package com.dmarquina.plantcare.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateLastDateActionRequest {
  LocalDate lastDateAction;
}
