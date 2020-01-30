package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("userService")
public class UserServiceImpl implements UserService {
  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public User createUpdateUser(User user) {
    try {
      return userRepository.save(setKeepMaxQuantityPlantsAndDisplayName(user));
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException(
          "Hubo un error interno al crear o actualizar el usuario.");
    }
  }

  @Override
  @Transactional
  public User getUser(String id) {
    try {
      return findUserById(id);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException("Hubo un error interno al obtener el usuario.");
    }
  }

  private User setKeepMaxQuantityPlantsAndDisplayName(User user) {
    try {
      Optional<User> userFoundOptional = userRepository.findById(user.getId());
      if (userFoundOptional.isPresent()) {
        User userFound = userFoundOptional.get();
        user.setMaxQuantityPlants(userFound.getMaxQuantityPlants());
        user.setDisplayName(userFound.getDisplayName());
      } else {
        user.setMaxQuantityPlants(Constants.MAX_QUANTITY_PLANTS_DEFAULT);
      }
    } catch (Exception e) {
      throw new PlantServerErrorException(
          "Hubo un error interno al crear o actualizar el usuario.");
    }
    return user;
  }

  private User findUserById(String id) {
    Optional<User> userFoundOptional = userRepository.findById(id);
    if (userFoundOptional.isPresent()) {
      return userFoundOptional.get();
    } else {
      log.info("Error en user ID: " + id);
      throw new PlantServerErrorException("Hubo un error interno al obtener el usuario.");
    }
  }
}
