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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("freeUserService")
public class FreeUserServiceImpl implements UserService {
  private final Logger log = LoggerFactory.getLogger(UserService.class);

  @Autowired
  private UserRepository userRepository;

  @Override
  public List<Plant> findAllMyPlants(String ownerId) {
    try {
      return userRepository.getAllPlantsAndRemindersByOwnerIdOrderByIdDesc(ownerId)
          .stream()
          .limit(Constants.MAX_QUANTITY_PLANTS_DEFAULT)
          .collect(Collectors.toList());
    } catch (Exception e) {
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  public List<Memory> findMemoriesByOwnerId(String ownerId) {
    try {
      List<Long> plantsIds = getUserPlantsIds(ownerId);
      log.info("memory for ownerId: " + ownerId);
      if(plantsIds.size()>0){
        return userRepository.getMyPlantsMemories(plantsIds);
      }else{
        return new ArrayList<>();
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  public Boolean verifyPrivilege(String userId, String action) {
    try {
      Optional<User> userOptional = userRepository.findById(userId);
      if (userOptional.isPresent()) {
        Boolean hasPrivilege;
        User userFound = userOptional.get();
        switch (action) {
          case Constants.CREATE_NEW_PLANT_ACTION:
            hasPrivilege =
                userRepository.getPlantsQuantity(userId) < userFound.getMaxQuantityPlants();
            break;
          case Constants.CREATE_NEW_MEMORY_ACTION:
            List<Long> plantsIds = getUserPlantsIds(userId);
            if(plantsIds.size()>0){
              hasPrivilege = userRepository.getMyPlantsMemories(plantsIds)
                  .size() < userFound.getMaxQuantityPlantMemories();
            }else {
              hasPrivilege = true;
            }
            break;
          default:
            log.info("verifyPrivilege() - No existe la acción solicitada: " + action);
            throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
        }
        return hasPrivilege;
      } else {
        throw new ResourceNotFoundException(Messages.USER_NOT_FOUND_EXCEPTION_MESSAGE);
      }
    } catch (Exception e) {
      e.printStackTrace();
      log.info("verifyPrivilege() - Error inesperado");
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  private List<Long> getUserPlantsIds(String ownerId) {
    try {
      log.info("getUserPlantsIds(String ownerId) - plants for ownerId: " + ownerId);
      return userRepository.getAllPlantsAndRemindersByOwnerIdOrderByIdDesc(ownerId)
          .stream()
          .limit(Constants.MAX_QUANTITY_PLANTS_DEFAULT)
          .map(Plant::getId)
          .collect(Collectors.toList());
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }
}