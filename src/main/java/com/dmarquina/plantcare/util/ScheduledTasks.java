package com.dmarquina.plantcare.util;

import com.dmarquina.plantcare.repository.ReminderRepository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class ScheduledTasks {

  @Autowired
  ReminderRepository reminderRepository;

  @Scheduled(cron = "0 0 9 * * ?")
  public void reportCurrentTime() {
    RestTemplate restTemplate = new RestTemplate();
    List<String> deviceTokensToRemind = reminderRepository.getUsersDeviceTokensToRemind();
    if (deviceTokensToRemind != null && !deviceTokensToRemind.isEmpty()) {
      MultiValueMap<String, String> req = new LinkedMultiValueMap();
      req.addAll("pushUserTokens", deviceTokensToRemind);
      restTemplate.postForEntity(
          "https://us-central1-watered-plant.cloudfunctions.net/webApi/api/v1/pushnotifications",
          req, String.class);
    }
  }
}