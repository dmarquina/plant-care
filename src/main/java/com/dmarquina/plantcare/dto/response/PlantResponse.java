package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.Reminder;
import com.dmarquina.plantcare.util.Constants;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class PlantResponse {
  private Long id;
  private String ownerId;
  private String name;
  private String image;
  private Set<ReminderResponse> reminders;

  public PlantResponse(Plant plant) {
    BeanUtils.copyProperties(plant, this);
    this.setReminders(plant.getReminders());
  }

  public void setReminders(Set<Reminder> reminders) {
    this.reminders = reminders.stream()
        .map(r -> {
          ReminderResponse rr = new ReminderResponse();
          BeanUtils.copyProperties(r, rr);
          return rr;
        })
        .collect(Collectors.toSet());
  }

  public String getImage() {
    return Constants.getImageURL(image);
  }
}
