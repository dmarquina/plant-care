package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Memory;

import java.util.List;

public interface MemoryService {

  List<Memory> findMemoryByPlantId(Long plantId);

  Memory create(Memory memory);
}
