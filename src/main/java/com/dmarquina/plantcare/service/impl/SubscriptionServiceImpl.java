package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.dto.request.SubscriptionRequest;
import com.dmarquina.plantcare.model.Subscription;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.SubscriptionRepository;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.SubscriptionService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.Messages;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("subscriptionService")
public class SubscriptionServiceImpl implements SubscriptionService {

  private final Logger log = LoggerFactory.getLogger(SubscriptionService.class);

  @Autowired
  private SubscriptionRepository subscriptionRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public Subscription subscribe(SubscriptionRequest subscriptionRequest) {
    User userFound = findUserById(subscriptionRequest.getUserId());
    Subscription subscription = userFound.getSubscription();
    if (subscription == null) {//create subscription
      subscription = setNewSubscription(subscriptionRequest, userFound);
    } else {//update subscription
      if (Constants.isSubscriptionInActivePeriod(subscription)) {
        subscription.setEndDate(subscription.getEndDate()
                                    .plusDays(subscriptionRequest.getDays()));
      } else {
        subscription.setEndDate(LocalDate.now()
                                    .plusDays(subscriptionRequest.getDays()));
      }
    }
    try {
      return subscriptionRepository.save(subscription);
    } catch (Exception e) {
      e.printStackTrace();
      log.info("subscribe(SubscriptionRequest subscriptionRequest): " + subscription);
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  private User findUserById(String id) {
    try {
      Optional<User> userFoundOptional = userRepository.findById(id);
      if (userFoundOptional.isPresent()) {
        return userFoundOptional.get();
      } else {
        log.info("Error en user ID: " + id);
        throw new ResourceNotFoundException(Messages.USER_NOT_FOUND_EXCEPTION_MESSAGE);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  private Subscription setNewSubscription(SubscriptionRequest subscriptionRequest, User userFound) {
    Subscription subscription = new Subscription();
    subscription.setStartDate(LocalDate.now());
    subscription.setEndDate(LocalDate.now()
                                .plusDays(subscriptionRequest.getDays()));
    try {
      userRepository.save(userFound);
      return subscription;
    } catch (Exception e) {
      e.printStackTrace();
      log.info("asvdsv: - " + userFound.getId());
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

}