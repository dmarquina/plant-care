package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Memory;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemoryRepository extends JpaRepository<Memory, Long> {

  List<Memory> findByPlantIdOrderByIdDesc(Long plantId);

  @Query(
      "SELECT m FROM User u INNER JOIN Plant p ON p.ownerId = u.id RIGHT JOIN Memory m on m.plantId = p.id  WHERE u.id = :ownerId ")
  List<Memory> findByOwnerId(@Param("ownerId") String ownerId);

  long deleteByPlantId(long plantId);
}
