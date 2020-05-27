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
import com.dmarquina.plantcare.util.Messages;
import com.dmarquina.plantcare.util.exceptionhandler.AmazonException;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;

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
      throw new ResourceNotFoundException(Messages.PLANT_NOT_FOUND_EXCEPTION_MESSAGE);
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
      plantCreated = plantRepository.saveAndFlush(plant);
    } catch (Exception e) {
      log.info("subscribe(Plant plant) - Hubo un problema al crear la planta");
      log.info("plant: " + plant);
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
    uploadNewPlantImage(plant, plantCreated, imageFile);
    try {
      return plantRepository.save(plantCreated);
    } catch (Exception e) {
      log.info("subscribe(Plant plant) - Hubo un problema al crear la planta con foto");
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Transactional
  private synchronized void uploadNewPlantImage(Plant plant, Plant plantCreated, File imageFile) {
    try {
      String fileName = AWSUtils.makeFileName(plant.getOwnerId(), (plantCreated.getId()
          .toString()));
      amazonService.uploadFile(AWSUtils.CURRENT_PHOTOS_BUCKET, fileName, imageFile);
      plantCreated.setImage(fileName);
    } catch (Exception e) {
      imageFile.delete();
      log.info(
          "uploadNewPlantImage(Plant plant, Plant plantCreated, File imageFile) - Hubo un problema al guardar la foto de la nueva planta");
      log.info("plant: " + plant);
      plantRepository.deleteById(plantCreated.getId());
      plantRepository.flush();
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  @Transactional
  public synchronized Plant update(Plant plant, List<Long> remindersToDelete) {
    updatePlantImage(plant);
    if (!remindersToDelete.isEmpty()) {
      remindersToDelete.stream()
          .forEach(reminderService::deleteById);
    }
    return plantRepository.save(plant);
  }

  @Override
  @Transactional
  public synchronized void delete(Long plantId) {
    Optional<Plant> opPlant = plantRepository.findById(plantId);
    if (opPlant.isPresent()) {
      Plant plantFound = opPlant.get();
      //Eliminar planta de la tabla plants
      try {
        plantRepository.deleteById(plantId);
      } catch (Exception e) {
        e.printStackTrace();
        throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
      }

      //Eliminar recuerdos de planta de la tabla memories
      try {
        memoryRepository.deleteByPlantId(plantId);
      } catch (Exception e) {
        e.printStackTrace();
        throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
      }

      try {
        //Eliminar foto de la planta en aws
        amazonService.deleteFile(AWSUtils.CURRENT_PHOTOS_BUCKET, plantFound.getImage());

        //Eliminar fotos de los recuedos de la planta en aws
        List<Memory> memories = memoryRepository.findByPlantIdOrderByIdDesc(plantId);
        memories.forEach(
            memory -> amazonService.deleteFile(AWSUtils.PHOTOS_MEMORIES_BUCKET, memory.getImage()));

      } catch (Exception e) {
        e.printStackTrace();
        throw new AmazonException(Messages.UPLOAD_IMAGE_EXCEPTION_MESSAGE);
      }
    } else {
      throw new ResourceNotFoundException(Messages.PLANT_NOT_FOUND_EXCEPTION_MESSAGE);
    }
  }

  @Override
  public List<Plant> findAllPlants() {
    return plantRepository.findAll();
  }

  private void validateImage(String imageFile) {
    if (imageFile == null || imageFile.equalsIgnoreCase("")) {
      log.info(
          "validateImage(String imageFile) - Hubo un problema con la imagen, parece estar vacia");
      throw new PlantCareServerErrorException("Es necesaria la imagen de tu planta");
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
          amazonService.deleteFile(AWSUtils.CURRENT_PHOTOS_BUCKET,
                                   AWSUtils.getImageNameOnly(AWSUtils.CURRENT_PHOTOS_BUCKET_PATH,
                                                             actualPlant.getImage()));
        } catch (Exception e) {
          e.printStackTrace();
          log.info("updatePlantImage(Plant plant) - Hubo un problema al eliminar la imagen");
          throw new AmazonException(Messages.UPLOAD_IMAGE_EXCEPTION_MESSAGE);
        }
        File imageFile = AWSUtils.createImageFileToUpload(plant.getImage());
        String fileName = AWSUtils.makeFileName(plant.getOwnerId(), plant.getId()
            .toString());
        try {
          amazonService.uploadFile(AWSUtils.CURRENT_PHOTOS_BUCKET, fileName, imageFile);
          plant.setImage(fileName);
        } catch (Exception e) {
          e.printStackTrace();
          log.error("Hubo un problema al subir la imagen");
          imageFile.delete();
          throw new AmazonException(Messages.UPLOAD_IMAGE_EXCEPTION_MESSAGE);
        }
      }
    } else {
      log.info("updatePlantImage(Plant plant) - Planta no existe: " + plant.getId());
      throw new ResourceNotFoundException(Messages.PLANT_NOT_FOUND_EXCEPTION_MESSAGE);
    }
  }
}