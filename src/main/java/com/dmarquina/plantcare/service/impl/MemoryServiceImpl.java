package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.repository.MemoryRepository;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.service.MemoryService;
import com.dmarquina.plantcare.util.AWSUtils;
import com.dmarquina.plantcare.util.Messages;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;

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

  @Override
  @Transactional
  public Memory create(Memory memory) {
      File imageFile = AWSUtils.createImageFileToUpload(memory.getImage());
      String fileName = AWSUtils.makeFileName(memory.getPlantId()
                                                  .toString());
    try {
      memory.setImage("");
      memory.setDate(LocalDate.now());
      memoryRepository.saveAndFlush(memory);
      return uploadMemoryImage(memory, fileName, imageFile);
    } catch (Exception e) {
      log.info("memory: " + memory);
      imageFile.delete();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }

  }

  @Override
  public List<Memory> findMemoriesByPlantId(Long plantId) {
    try {
      log.info("memory for plantId: " + plantId);
      return memoryRepository.findByPlantIdOrderByIdDesc(plantId);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Transactional
  private synchronized Memory uploadMemoryImage(Memory memory, String fileName, File imageFile) {
    try {
      amazonService.uploadFile(AWSUtils.PHOTOS_MEMORIES_BUCKET, fileName, imageFile);
      memory.setImage(fileName);
      return memoryRepository.save(memory);
    } catch (Exception e) {
      imageFile.delete();
      log.info("memory: " + memory);
      memoryRepository.deleteById(memory.getId());
      memoryRepository.flush();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }
}