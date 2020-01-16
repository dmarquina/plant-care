package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.Reminder;
import com.dmarquina.plantcare.util.Constants;

import java.util.Set;
import java.util.stream.Collectors;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class PlantAdminResponse {
  private Long id;
  private String ownerId;
  private String name;
  private String image;

  public PlantAdminResponse(Plant plant) {
    BeanUtils.copyProperties(plant, this);
  }

  public String getImage() {
    return Constants.getImageURL(image);
  }
}
