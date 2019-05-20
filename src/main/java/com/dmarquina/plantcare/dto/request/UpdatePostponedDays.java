package com.dmarquina.plantcare.dto.request;

import java.util.List;

import lombok.Data;

@Data
public class UpdatePostponedDays {
  List<Long> reminderIds;
  long postponedDays;
}
