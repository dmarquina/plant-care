package com.dmarquina.plantcare.model;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
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
  private LocalDate lastLoginDate;
  private int maxQuantityPlants;
  private int maxQuantityPlantMemories;
  @OneToOne(cascade = CascadeType.ALL)
  private Subscription subscription;

}
