package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.dto.request.SubscriptionRequest;
import com.dmarquina.plantcare.model.Subscription;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.SubscriptionRepository;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.SubscriptionService;
import com.dmarquina.plantcare.util.exceptionhandler.CustomException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class SubscribeUserTest {
  @TestConfiguration
  static class SubscribeUserTestContextConfiguration {
    @Bean
    public SubscriptionService subscriptionService() {
      return new SubscriptionServiceImpl();
    }
  }

  @Autowired
  SubscriptionService subscriptionService;

  @MockBean
  SubscriptionRepository subscriptionRepository;

  @MockBean
  UserRepository userRepository;

  @Before
  public void setUp() {

    User user = new User();
    user.setId("OYE1");
    user.setVerificationCode("CODEVER");
    Set<Subscription> subscriptionList = new HashSet<>();
    Subscription subscription = new Subscription();
    subscription.setStartDate(LocalDate.now());
    subscriptionList.add(subscription);
    Mockito.when(userRepository.findById(user.getId()))
        .thenReturn(Optional.of(user));
  }
//
//  @Test(expected = PlantCareServerErrorException.class)
//  public void userNotFound() {
//
//    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
//    subscriptionRequest.setUserId("OYE1");
//    subscriptionRequest.setDays(30);
//    subscriptionService.subscribe(subscriptionRequest);
//  }
//
//  @Test(expected = CustomException.class)
//  public void newSubscription() {
//    SubscriptionRequest subscriptionRequest = new SubscriptionRequest();
//    subscriptionRequest.setUserId("OYE1");
//    subscriptionRequest.setDays(30);
//    subscriptionService.subscribe(subscriptionRequest);
//  }

}
