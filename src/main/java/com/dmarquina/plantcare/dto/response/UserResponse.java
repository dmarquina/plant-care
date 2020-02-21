package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.User;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class UserResponse {
  private String id;
  private String deviceToken;
  private String email;
  private int maxQuantityPlants;
  private int maxQuantityPlantMemories;
  private String displayName;
  private String photoUrl;

  public UserResponse(User user) {
    BeanUtils.copyProperties(user, this);
  }
}
