package com.dmarquina.plantcare.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.ToString;

@Data
@Entity
@ToString
@Table(name = "user")
public class User {
  @Id
  private String id;
  private String deviceToken;
  private String email;
  private String displayName;
  private String photoUrl;
  private int maxQuantityPlants;
  private int maxQuantityPlantMemories;
}
