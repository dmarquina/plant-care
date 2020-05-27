package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Subscription;

import java.time.LocalDate;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class SubscriptionResponse {
  private LocalDate endDate;

  public SubscriptionResponse(Subscription subscription) {
    BeanUtils.copyProperties(subscription, this);
  }
}
