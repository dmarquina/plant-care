package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Memory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

  List<Memory> findByPlantId(Long plantId);
}
