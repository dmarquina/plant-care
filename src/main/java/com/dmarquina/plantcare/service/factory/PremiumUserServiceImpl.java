package com.dmarquina.plantcare.service.factory;

import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.Messages;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("premiumUserService")
public class PremiumUserServiceImpl implements UserService {
  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Override
  public List<Plant> findAllMyPlants(String ownerId) {
    try {
      return userRepository.getAllPlantsAndRemindersByOwnerIdOrderByIdDesc(ownerId);
    } catch (Exception e) {
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  public List<Memory> findMemoriesByOwnerId(String ownerId) {
    try {
      log.info("memory for ownerId: " + ownerId);
      return userRepository.getMemoriesByUserId(ownerId);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  public Boolean verifyPrivilege(String id, String action) {
    Optional<User> userOptional = userRepository.findById(id);
    if (userOptional.isPresent()) {
      Boolean hasPrivilege;
      User userFound = userOptional.get();
      switch (action) {
        case Constants.CREATE_NEW_PLANT_ACTION:
          hasPrivilege = userRepository.getPlantsQuantity(id) < userFound.getMaxQuantityPlants();
          break;
        case Constants.CREATE_NEW_MEMORY_ACTION:
          hasPrivilege =
              userRepository.getMemoriesQuantity(id) < userFound.getMaxQuantityPlantMemories();
          break;
        default:
          log.info("verifyPrivilege() - No existe la acción solicitada: " + action);
          throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
      }
      return hasPrivilege;
    } else {
      throw new ResourceNotFoundException(Messages.USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }
  }

}