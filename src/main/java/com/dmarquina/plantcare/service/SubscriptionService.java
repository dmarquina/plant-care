package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.dto.request.SubscriptionRequest;
import com.dmarquina.plantcare.model.Subscription;

public interface SubscriptionService {

  Subscription subscribe(SubscriptionRequest subscriptionRequest);

}
