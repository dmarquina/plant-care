package com.dmarquina.plantcare.dto.request;

import com.dmarquina.plantcare.model.Reminder;
import com.dmarquina.plantcare.util.Constants;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class PlantRequest {

  private long id;
  @NotEmpty(message = "Es necesario el id del dueño")
  private String ownerId;
  @NotEmpty(message = "Tu planta necesita un nombre")
  private String name;
  private String image;
  private List<ReminderRequest> reminders;
  private List<Long> remindersToDelete;

  public Set<Reminder> getReminders() {
    return reminders.stream()
        .map(rr -> {
          Reminder r = new Reminder();
          BeanUtils.copyProperties(rr, r);
          return r;
        })
        .collect(Collectors.toSet());
  }

  public String getImage() {
    return this.image != null ? Constants.getImageNameFromURL(this.image) : "";
  }
}
