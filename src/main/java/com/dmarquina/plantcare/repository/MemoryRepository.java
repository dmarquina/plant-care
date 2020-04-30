package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Memory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

  List<Memory> findByPlantIdOrderByIdDesc(Long plantId);

  long deleteByPlantId(long plantId);
}
