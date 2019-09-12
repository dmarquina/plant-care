package com.dmarquina.plantcare.model;

import java.time.LocalDate;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "reminder_executed")
public class ReminderExecuted {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private Long reminderId;
  private LocalDate dateAction;

  public ReminderExecuted() {
  }

  public ReminderExecuted(Long reminderId, LocalDate dateAction) {
    this.reminderId = reminderId;
    this.dateAction = dateAction;
  }
}
