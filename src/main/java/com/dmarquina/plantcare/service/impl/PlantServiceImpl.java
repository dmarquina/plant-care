package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.repository.PlantRepository;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.service.PlantService;
import com.dmarquina.plantcare.service.ReminderService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.exceptionhandler.AmazonException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantNotFoundException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Optional;

import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("plantService")
public class PlantServiceImpl implements PlantService {
  //TODO: Logs
  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlantRepository plantRepository;

  @Autowired
  AmazonService amazonService;

  @Autowired
  ReminderService reminderService;

  @Override
  public List<Plant> findAllMyPlants(String ownerId) {
    return plantRepository.getAllPlantsAndRemindersByOwnerIdOrderByIdDesc(ownerId);
  }

  @Override
  public Plant findById(Long id) {
    Optional<Plant> opPlant = plantRepository.findById(id);
    if (opPlant.isPresent()) {
      return plantRepository.findById(id)
          .get();
    } else {
      throw new PlantNotFoundException("No se encontró la planta.");

    }
  }

  @Override
  @Transactional
  public Plant create(Plant plant) {
    validateImage(plant.getImage());
    File imageFile = createImageToUpload(plant.getImage());
    plant.setImage("");
    Plant plantCreated;
    try {
      plantCreated = plantRepository.save(plant);
    } catch (Exception e) {
      throw new PlantServerErrorException("Hubo un error interno al crear la planta.");
    }
    try {
      String fileName =
          amazonService.uploadFile(plantCreated.getId(), plantCreated.getOwnerId(), imageFile);
      plantCreated.setImage(fileName);
    } catch (Exception e) {
      plantRepository.deleteById(plantCreated.getId());
      throw new PlantServerErrorException("Hubo un error interno al subir la imagen de tu planta.");
    }
    try {
      return plantRepository.save(plantCreated);
    } catch (Exception e) {
      //TODO: eliminar foto porseacaso
      throw new PlantServerErrorException("Hubo un error interno al crear la planta.");
    }
  }

  @Override
  @Transactional
  public Plant update(Plant plant, List<Long> remindersToDelete) {
    updatePlantImage(plant);
    if (!remindersToDelete.isEmpty()) {
      remindersToDelete.stream()
          .forEach(reminderService::deleteById);
    }
    return plantRepository.save(plant);
  }

  @Override
  @Transactional
  public void delete(Long id) {
    Optional<Plant> opPlant = plantRepository.findById(id);
    if (opPlant.isPresent()) {
      Plant plantFound = opPlant.get();
      try {
        plantRepository.deleteById(id);
      } catch (Exception e) {
        e.printStackTrace();
        throw new PlantServerErrorException("Hubo un error interno al eliminar la planta.");
      }
      try {
        amazonService.deleteFile(plantFound.getImage());
      } catch (Exception e) {
        e.printStackTrace();
        throw new AmazonException("Hubo un problema al eliminar la imagen.");
      }
    } else {
      throw new PlantNotFoundException("Esta planta no existe.");
    }
  }

  @Override
  public List<Plant> findAllPlants() {
    return plantRepository.findAll();
  }

  private void validateImage(String imageFile) {
    if (imageFile == null || imageFile.equalsIgnoreCase("")) {
      throw new PlantServerErrorException("Es necesaria la imagen de tu plantita");
    }
  }

  private File createImageToUpload(String imageBase64) {
    byte[] decodedBytes = Base64.decodeBase64(imageBase64);
    File imageFile = new File("image");
    try {
      FileOutputStream fos = new FileOutputStream(imageFile);
      fos.write(decodedBytes);
      fos.close();
      return imageFile;
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException("Error con la imagen de la planta.");
    }
  }

  private void updatePlantImage(Plant plant) {
    validateImage(plant.getImage());
    Optional<Plant> optionalPlant = plantRepository.findById(plant.getId());
    if (optionalPlant.isPresent()) {
      Plant actualPlant = optionalPlant.get();
      if (!actualPlant.getImage()
          .equalsIgnoreCase(plant.getImage())) {
        try {
          amazonService.deleteFile(Constants.getImageNameFromURL(actualPlant.getImage()));
          File imageFile = createImageToUpload(plant.getImage());
          String fileName = amazonService.uploadFile(plant.getId(), plant.getOwnerId(), imageFile);
          plant.setImage(fileName);
        } catch (Exception e) {
          e.printStackTrace();
          throw new AmazonException("Hubo un problema al actualizar la imagen.");
        }
      }
    } else {
      //TODO: log
      throw new PlantServerErrorException("Hubo un error interno.");
    }
  }
}