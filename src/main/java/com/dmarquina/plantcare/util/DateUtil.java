package com.dmarquina.plantcare.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {

  public static Long getDaysSinceLastWatering(LocalDate lastDayWatering) {
    return lastDayWatering.until(LocalDate.now(), ChronoUnit.DAYS);
  }
}
