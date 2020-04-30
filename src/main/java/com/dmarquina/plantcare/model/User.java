package com.dmarquina.plantcare.model;

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
  private String verificationCode;
  private Boolean isEmailVerified;
  private int maxQuantityPlants;
  private int maxQuantityPlantMemories;
}
