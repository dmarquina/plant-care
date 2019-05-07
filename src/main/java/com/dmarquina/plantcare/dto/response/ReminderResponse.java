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
  private long daysWithoutAction;
  private long daysRemainingForAction;

  private LocalDate lastDateAction;

  public long getDaysRemainingForAction() {
    LocalDate dateToAct = lastDateAction.plusDays(frequencyDays)
        .plusDays(postponedDays);

    return LocalDate.now()
        .until(dateToAct, ChronoUnit.DAYS);
  }

  public long getDaysWithoutAction() {
    return lastDateAction.until(LocalDate.now(), ChronoUnit.DAYS);
  }
}
