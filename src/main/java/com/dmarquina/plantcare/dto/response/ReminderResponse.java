package com.dmarquina.plantcare.dto.response;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import lombok.Data;

@Data
public class ReminderResponse {
  private Long id;
  private String name;
  private long frequencyDays;
  private long postponedDays;
  private long daysRemainingForAction;

  private LocalDate lastDayAction;

  public long getDaysRemainingForAction() {
    LocalDate dateToAct = lastDayAction.plusDays(frequencyDays)
        .plusDays(postponedDays);

    return LocalDate.now()
        .until(dateToAct, ChronoUnit.DAYS);
  }
}
