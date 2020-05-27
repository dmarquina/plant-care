package com.dmarquina.plantcare.service.factory;

import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.Messages;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userServiceFactory")
public class UserServiceFactory {

  @Autowired
  UserRepository userRepository;
  @Autowired
  UserService freeUserService;

  @Autowired
  UserService premiumUserService;

  public UserService getUserServiceImpl(String userId) {
    Optional<User> userOptional = userRepository.findById(userId);
    if (userOptional.isPresent()) {
      if (Constants.isSubscriptionInActivePeriod(userOptional.get()
                                                     .getSubscription())) {
        return premiumUserService;
      } else {
        return freeUserService;
      }
    } else {
      throw new PlantCareServerErrorException(Messages.USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }
  }
}
