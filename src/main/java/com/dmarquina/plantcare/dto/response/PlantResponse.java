package com.dmarquina.plantcare.dto.response;

import java.time.LocalDate;

import lombok.Data;

@Data
public class PlantResponse {
  private Long id;
  private String name;
  private String image;
  private Long minWateringDays;
  private Long maxWateringDays;
  private Long daysSinceLastDayWatering;
  private LocalDate lastDayWatering;
}
