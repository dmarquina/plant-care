package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.Plant;

import java.util.List;

public interface UserService {

  List<Plant> findAllMyPlants(String ownerId);

  List<Memory> findMemoriesByOwnerId(String ownerId);

  Boolean verifyPrivilege(String id, String action);

}
