package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.repository.PlantRepository;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.service.PlantService;
import com.dmarquina.plantcare.service.ReminderService;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.BeanUtils;
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
    return plantRepository.findById(id).get();
  }

  @Override
  @Transactional
  public Plant create(Plant plant) {
    Plant plantCreated = new Plant();
    BeanUtils.copyProperties(plantRepository.save(plant),plantCreated);
    plantCreated.getReminders().stream().forEach(reminder -> {
      reminderService.create(reminder);
    });
    return plantCreated;
  }

  @Override
  @Transactional
  public Plant update(Plant plant) {
    return plantRepository.save(plant);
  }


  @Override
  @Transactional
  public Plant updateImagePlant(Long id, MultipartFile newImage) {
    try {
      //TODO: Validar si existe o no
      Plant plantFound = plantRepository.findById(id).get();
      plantFound.setImage(amazonService.uploadFile(id, newImage));
      return plantRepository.save(plantFound);
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
