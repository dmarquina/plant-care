package com.dmarquina.plantcare.model;

import java.util.HashSet;
import java.util.Set;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(exclude = "reminders")
@Data
@Entity
@ToString
@Table(name = "plant")
public class Plant {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String ownerId;
  private String name;
  private String image;
  @OneToMany(cascade = CascadeType.ALL)
  @JoinColumn(name = "idPlant")
  private Set<Reminder> reminders = new HashSet<>();

  public Plant() {
  }

}
