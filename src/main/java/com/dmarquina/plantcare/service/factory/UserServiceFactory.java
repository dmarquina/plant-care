package com.dmarquina.plantcare.service.factory;

import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;

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
    User u = userRepository.findById(userId)
        .get();
    if (u != null) {
      return freeUserService;
    } else {
      return premiumUserService;
    }
  }
}
