package com.dmarquina.plantcare.dto.request;

import com.dmarquina.plantcare.model.Reminder;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class NewPlantRequest {
  private String ownerId;
  private String name;
  private Set<ReminderRequest> reminders;

  public Set<Reminder> getReminders() {
    return reminders.stream()
        .map(rr -> {
          Reminder r = new Reminder();
          BeanUtils.copyProperties(rr, r);
          return r;
        })
        .collect(Collectors.toSet());
  }

}
