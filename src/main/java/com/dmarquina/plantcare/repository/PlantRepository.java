package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Plant;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface PlantRepository extends JpaRepository<Plant, Long> {

  @Query(value = "SELECT p FROM Plant p WHERE p.ownerId IN :ownerIds")
  List<Plant> getAllPlantsByOwnerIds(@Param("ownerIds") Collection<String> ownerIds);

  @Modifying
  @Query("UPDATE Plant p SET p.image = :image WHERE p.id = :id")
  int updatePlantImage(@Param("id") Long id, @Param("image") String image);
}
