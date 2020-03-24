package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.User;

import java.util.List;

public interface UserService {

  User createUpdateUser(User user);

  List<Plant> findAllMyPlants(String ownerId);

  User getUser(String id);
}
