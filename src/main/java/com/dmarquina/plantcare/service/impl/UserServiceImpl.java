package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import java.util.NoSuchElementException;

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
      return userRepository.findById(id).get();
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException(
          "Hubo un error interno al obtener el usuario.");
    }
  }

  private User setKeepMaxQuantityPlantsAndDisplayName(User user) {
    try {
      User userFound = userRepository.findById(user.getId()).get();
      user.setMaxQuantityPlants(userFound.getMaxQuantityPlants());
      user.setDisplayName(userFound.getDisplayName());
    } catch (NoSuchElementException ne) {
      user.setMaxQuantityPlants(Constants.MAX_QUANTITY_PLANTS_DEFAULT);
    } catch (Exception e) {
      throw new PlantServerErrorException(
          "Hubo un error interno al crear o actualizar el usuario.");
    }
    return user;
  }






}
