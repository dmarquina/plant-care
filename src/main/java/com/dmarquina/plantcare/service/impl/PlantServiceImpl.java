package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.repository.PlantRepository;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.service.PlantService;

import java.time.LocalDate;
import java.util.List;

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

  @Override
  public List<Plant> findAllMyPlants(String userId) {
    return plantRepository.findAllByUserIdOrderByIdDesc(userId);
  }

  @Override
  public Plant findById(Long id) {
    return plantRepository.findById(id).get();
  }

  @Override
  @Transactional
  public Plant create(Plant plant) {
    return plantRepository.save(plant);
  }

  @Override
  @Transactional
  public Plant update(Plant plant) {
    return plantRepository.save(plant);
  }

  @Override
  @Transactional
  public Plant updateLastDayWatering(Long id, LocalDate lastDayWatering) {
    Plant plant = plantRepository.getOne(id);
    plant.setLastDayWatering(lastDayWatering);
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
