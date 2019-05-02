package com.dmarquina.plantcare.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "plantcare")
@Data
public class Plant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String userId;
  private String name;
  private String image;
  private Long minWateringDays;
  private Long maxWateringDays;
  private LocalDate lastDayWatering;

  public Plant() {
  }
}
