package com.dmarquina.plantcare.dto.request;

import java.time.LocalDate;

import lombok.Data;

@Data
public class NewPlantRequest {
  private String userId;
  private String name;
  private String image;
  private Long minWateringDays;
  private Long maxWateringDays;
  private LocalDate lastDayWatering;

}
