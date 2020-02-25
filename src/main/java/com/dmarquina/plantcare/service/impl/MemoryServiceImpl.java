package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.MemoryRepository;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.service.MemoryService;
import com.dmarquina.plantcare.util.AWSUtils;
import com.dmarquina.plantcare.util.exceptionhandler.MemoryNotEnoughSpaceException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import java.io.File;
import java.time.LocalDate;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("memoryService")
public class MemoryServiceImpl implements MemoryService {

  private final Logger log = LoggerFactory.getLogger(MemoryService.class);

  @Autowired
  AmazonService amazonService;

  @Autowired
  private MemoryRepository memoryRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  public List<Memory> findMemoriesByPlantId(Long plantId) {
    try {
      log.info("memory for plantId: " + plantId);
      return memoryRepository.findByPlantIdOrderByIdDesc(plantId);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  @Transactional
  public Memory create(Memory memory) {
    if(!userHaveEnoughSpaceForMemories(memory)){
      throw new MemoryNotEnoughSpaceException("Obten más espacio para tus recuerdos");
    }
    try {
      File imageFile = AWSUtils.createImageFileToUpload(memory.getImage());
      String fileName = AWSUtils.makeFileName(memory.getPlantId().toString());
      amazonService.uploadFile(AWSUtils.PHOTOS_MEMORIES_BUCKET, fileName, imageFile);
      memory.setImage(fileName);
      memory.setDate(LocalDate.now());
      return memoryRepository.save(memory);
    } catch (Exception e) {
      log.info("memory: " + memory);
      e.printStackTrace();
      throw new PlantServerErrorException("Hubo un error interno al subir la imagen del recuerdo");
    }
  }

  private boolean userHaveEnoughSpaceForMemories(Memory memory){
    User user = userRepository.getUserByPlantId(memory.getPlantId());
    List<Memory> usersMemories = memoryRepository.findByOwnerId(user.getId());
    return user.getMaxQuantityPlantMemories() > usersMemories.size() ;
  }
}