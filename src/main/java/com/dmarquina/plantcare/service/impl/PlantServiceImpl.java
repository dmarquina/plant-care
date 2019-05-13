package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.repository.PlantRepository;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.service.PlantService;
import com.dmarquina.plantcare.service.ReminderService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.exceptionhandler.AmazonException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantNotFoundException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Transactional(readOnly = true)
@Service("plantService")
public class PlantServiceImpl implements PlantService {

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
    try {
      return plantRepository.save(plant);
    } catch (Exception e) {
      throw new PlantServerErrorException("Hubo un error interno al crear la planta.");
    }
  }

  @Override
  @Transactional
  public Plant update(Plant plant, List<Long> remindersToDelete) {
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
      amazonService.deleteFile(plantFound.getImage());
    } catch (Exception e) {
      e.printStackTrace();
      throw new AmazonException("Hubo un problema al eliminar la imagen.");
    }
    try {
      plantRepository.deleteById(id);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException("Hubo un error interno al eliminar la planta.");
    }
    } else {
      throw new PlantNotFoundException("Esta planta no existe.");
    }
  }

  @Override
  @Transactional
  public Plant addImagePlant(Long id, MultipartFile newImage) {
    Optional<Plant> opPlant = plantRepository.findById(id);
    if (opPlant.isPresent()) {
      Plant plantFound = opPlant.get();
      try {
        plantFound.setImage(amazonService.uploadFile(id, newImage));
      } catch (Exception e) {
        e.printStackTrace();
        throw new AmazonException("Hubo un problema al subir la imagen.");
      }
      try {
        return plantRepository.save(plantFound);
      } catch (Exception e) {
        e.printStackTrace();
        throw new PlantServerErrorException(
            "Hubo un error interno al actualizar la imagen en la planta.");
      }
    } else {
      throw new PlantNotFoundException("No se encontró la planta.");
    }
  }

  @Override
  @Transactional
  public int updateImagePlant(Long id, String imageURL, MultipartFile newImage) {
    String newImageName;
    try {
      amazonService.deleteFile(Constants.getImageNameFromURL(imageURL));
      newImageName = amazonService.uploadFile(id, newImage);
    } catch (Exception e) {
      e.printStackTrace();
      throw new AmazonException("Hubo un problema al actualizar la imagen.");
    }
    try {
      return plantRepository.updatePlantImage(id, newImageName);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException(
          "Hubo un error interno al actualizar la imagen en la planta.");
    }
  }
}
