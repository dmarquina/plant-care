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
  private LocalDate dateToAction;

  private LocalDate lastDateAction;

  public LocalDate getDateToAction() {
    return lastDateAction.plusDays(frequencyDays)
        .plusDays(postponedDays);

  }

  public long getDaysWithoutAction() {
    return lastDateAction.until(LocalDate.now(), ChronoUnit.DAYS);
  }
}
