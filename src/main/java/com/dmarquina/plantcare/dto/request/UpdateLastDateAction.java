package com.dmarquina.plantcare.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class UpdateLastDateAction {
  LocalDate lastDateAction;
}
