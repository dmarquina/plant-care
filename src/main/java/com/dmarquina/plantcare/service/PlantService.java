package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Plant;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface PlantService {

  List<Plant> findAllMyPlants(String ownerId);

  Plant findById(Long id);

  Plant create(Plant plant);

  Plant update(Plant plant);

  Plant updateImagePlant(Long id, MultipartFile newImage);
}
