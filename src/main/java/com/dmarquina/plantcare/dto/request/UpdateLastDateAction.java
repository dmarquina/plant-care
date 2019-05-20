package com.dmarquina.plantcare.dto.request;

import java.time.LocalDate;
import java.util.List;

import lombok.Data;

@Data
public class UpdateLastDateAction {
  List<Long> reminderIds;
  LocalDate lastDateAction;
}
