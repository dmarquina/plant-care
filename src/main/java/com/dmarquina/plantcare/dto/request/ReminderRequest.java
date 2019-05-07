package com.dmarquina.plantcare.dto.request;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.Data;

@Data
public class ReminderRequest {
  private Long id;
  private String name;
  private long frequencyDays;
  private long postponedDays;
  private LocalDate lastDateAction;


}
