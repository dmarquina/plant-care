package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Plant;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

@Data
public class GardenPlantResponse {
  private List<PlantResponse> todayPlants;
  private List<PlantResponse> gardenPlants;

  public GardenPlantResponse(List<Plant> plants) {
    this.gardenPlants = plants.stream()
        .map(PlantResponse::new)
        .collect(Collectors.toList());
    this.todayPlants = plants.stream()
        .map(PlantResponse::new)
        .map(this::getRemindersNeedAction)
        .filter(plantResponse -> !plantResponse.getReminders()
            .isEmpty())
        .collect(Collectors.toList());

  }

  private PlantResponse getRemindersNeedAction(PlantResponse plantResponse) {
    List<ReminderResponse> reminderResponsesToRemove = plantResponse.getReminders()
        .stream()
        .filter(this::doesThisReminderNeedNoActionYet)
        .collect(Collectors.toList());
    plantResponse.getReminders()
        .removeAll(reminderResponsesToRemove);
    return plantResponse;
  }

  private boolean doesThisReminderNeedNoActionYet(ReminderResponse reminderResponse) {
    return LocalDate.now()
        .until(reminderResponse.getDateToAction(), ChronoUnit.DAYS) > 0;
  }
}
