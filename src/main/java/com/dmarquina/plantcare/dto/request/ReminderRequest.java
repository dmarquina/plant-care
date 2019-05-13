package com.dmarquina.plantcare.dto.request;

import java.time.LocalDate;
import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
@Valid
public class ReminderRequest {
  private Long id;
  @NotEmpty(message = "Es necesario el nombre del recordatorio")
  private String name;
  @Min(value = 1, message = "La frecuencia debe ser de al menos 1 día")
  private long frequencyDays;
  private long postponedDays;
  @NotEmpty(message = "Es necesario la última fecha de acción")
  private LocalDate lastDateAction;

}
