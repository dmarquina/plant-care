package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Plant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {
  List<Plant> findAllByUserIdOrderByIdDesc(String userId);
}
