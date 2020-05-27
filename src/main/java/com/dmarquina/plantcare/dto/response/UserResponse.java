package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.User;

import java.time.LocalDate;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class UserResponse {
  private String id;
  private String deviceToken;
  private String email;
  private Boolean isEmailVerified;
  private int maxQuantityPlants;
  private int maxQuantityPlantMemories;
  private String displayName;
  private String photoUrl;
  private LocalDate subscriptionEndDate;

  public UserResponse(User user) {
    BeanUtils.copyProperties(user, this);
    if (user.getSubscription() != null) {
      this.setSubscriptionEndDate(user.getSubscription()
                                      .getEndDate());
    }
  }
}
