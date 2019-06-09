package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("userService")
public class UserServiceImpl implements UserService {

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public User createUpdateUser(User user) {
    try {
      return userRepository.save(user);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException(
          "Hubo un error interno al crear o actualizar el usuario.");
    }
  }
}
