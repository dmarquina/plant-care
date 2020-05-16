package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Memory;

import java.util.List;

public interface MemoryService {

  List<Memory> findMemoriesByPlantId(Long plantId);

  Memory create(Memory memory);

}
