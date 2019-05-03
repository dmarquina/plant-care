package com.dmarquina.plantcare.dto.request;

import com.dmarquina.plantcare.model.Reminder;
import com.sun.xml.internal.ws.developer.Serialization;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
@Serialization
public class NewPlantRequest {
  private String ownerId;
  private String name;
  private List<ReminderRequest> reminders;

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
