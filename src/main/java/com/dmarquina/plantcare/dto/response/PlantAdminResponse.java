package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.util.AWSUtils;

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
    return AWSUtils.getImageURL(image);
  }
}
