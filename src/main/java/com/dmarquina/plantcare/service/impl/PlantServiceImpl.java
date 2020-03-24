package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.repository.MemoryRepository;
import com.dmarquina.plantcare.repository.PlantRepository;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.service.PlantService;
import com.dmarquina.plantcare.service.ReminderService;
import com.dmarquina.plantcare.util.AWSUtils;
import com.dmarquina.plantcare.util.exceptionhandler.AmazonException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantNotFoundException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import java.io.File;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("plantService")
public class PlantServiceImpl implements PlantService {

  private final Logger log = LoggerFactory.getLogger(PlantService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private PlantRepository plantRepository;

  @Autowired
  private MemoryRepository memoryRepository;

  @Autowired
  AmazonService amazonService;

  @Autowired
  ReminderService reminderService;

  @Override
  public Plant findById(Long id) {
    Optional<Plant> opPlant = plantRepository.findById(id);
    if (opPlant.isPresent()) {
      return opPlant.get();
    } else {
      throw new PlantNotFoundException("No se encontró la planta.");
    }
  }

  @Override
  @Transactional
  public Plant create(Plant plant) {
    validateImage(plant.getImage());
    File imageFile = AWSUtils.createImageFileToUpload(plant.getImage());
    plant.setImage("");
    plant.setCreationDate(LocalDate.now());
    Plant plantCreated;
    try {
      plantCreated = plantRepository.save(plant);
    } catch (Exception e) {
      throw new PlantServerErrorException("Hubo un error interno al crear la planta.");
    }
    try {
      log.info("Iniciando subida de imagen");
      String fileName = AWSUtils.makeFileName(plant.getOwnerId(), (plantCreated.getId()
          .toString()));
      amazonService.uploadFile(AWSUtils.CURRENT_PHOTOS_BUCKET, fileName, imageFile);
      log.info("Finalizando subida de imagen");
      plantCreated.setImage(fileName);
    } catch (Exception e) {
      plantRepository.deleteById(plantCreated.getId());
      e.printStackTrace();
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
  public void delete(Long plantId) {
    Optional<Plant> opPlant = plantRepository.findById(plantId);
    if (opPlant.isPresent()) {
      Plant plantFound = opPlant.get();

      //Eliminar planta de la tabla plants
      try {
        plantRepository.deleteById(plantId);
      } catch (Exception e) {
        e.printStackTrace();
        throw new PlantServerErrorException("Hubo un error interno al eliminar la planta.");
      }

      //Eliminar recuerdos de planta de la tabla memories
      try {
        memoryRepository.deleteByPlantId(plantId);
      } catch (Exception e) {
        e.printStackTrace();
        throw new PlantServerErrorException(
            "Hubo un error interno al eliminar los recuerdos de planta.");
      }

      try {

        //Eliminar foto de la planta en aws
        amazonService.deleteFile(AWSUtils.CURRENT_PHOTOS_BUCKET, plantFound.getImage());

        //Eliminar foto de los recuedos de la planta en aws
        List<Memory> memories = memoryRepository.findByPlantIdOrderByIdDesc(plantId);
        memories.forEach(
            memory -> amazonService.deleteFile(AWSUtils.PHOTOS_MEMORIES_BUCKET, memory.getImage()));

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

  private void updatePlantImage(Plant plant) {
    validateImage(plant.getImage());
    Optional<Plant> optionalPlant = plantRepository.findById(plant.getId());
    if (optionalPlant.isPresent()) {
      Plant actualPlant = optionalPlant.get();
      if(1>=0)
      if (!actualPlant.getImage()
          .equalsIgnoreCase(plant.getImage())) {
        try {
          amazonService.deleteFile(AWSUtils.CURRENT_PHOTOS_BUCKET,
                                   AWSUtils.getImageNameOnly(AWSUtils.CURRENT_PHOTOS_BUCKET_PATH,
                                                             actualPlant.getImage()));
        } catch (Exception e) {
          e.printStackTrace();
          log.info("Hubo un problema al eliminar la imagen");
        }
        try {
          File imageFile = AWSUtils.createImageFileToUpload(plant.getImage());
          String fileName = AWSUtils.makeFileName(plant.getOwnerId(), plant.getId()
              .toString());
          amazonService.uploadFile(AWSUtils.CURRENT_PHOTOS_BUCKET, fileName, imageFile);
          plant.setImage(fileName);
        } catch (Exception e) {
          e.printStackTrace();
          log.error("Hubo un problema al subir la imagen");
          throw new AmazonException("Hubo un problema subir la imagen.");
        }
      }
    } else {
      //TODO: log
      throw new PlantServerErrorException("Hubo un error interno.");
    }
  }
}