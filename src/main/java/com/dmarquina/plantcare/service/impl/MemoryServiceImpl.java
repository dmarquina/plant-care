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
    validateImage(memory.getImage());
    File imageFile = AWSUtils.createImageFileToUpload(memory.getImage());
    memory.setImage("");
    memory.setDate(LocalDate.now());
    Memory memoryCreated;
    try {
      memoryCreated = memoryRepository.saveAndFlush(memory);
    } catch (Exception e) {
      log.info("makeSale(Memory memory) - Hubo un problema al crear el recuerdo");
      log.info("memory: " + memory);
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
    uploadMemoryImage(memory, memoryCreated, imageFile);
    try {
      return memoryRepository.save(memoryCreated);
    } catch (Exception e) {
      log.info("makeSale(Memory memory) - Hubo un problema al crear el recuerdo con foto");
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
  private synchronized void uploadMemoryImage(Memory memory, Memory memoryCreated, File imageFile) {
    try {
      String fileName = AWSUtils.makeFileName(memory.getPlantId()
                                                  .toString(), memoryCreated.getId()
                                                  .toString());
      amazonService.uploadFile(AWSUtils.PHOTOS_MEMORIES_BUCKET, fileName, imageFile);
      memoryCreated.setImage(fileName);
    } catch (Exception e) {
      imageFile.delete();
      log.info(
          "uploadMemoryImage(Memory memory, Memory memoryCreated,File imageFile) - Hubo un problema al guardar la foto del recuerdo");
      log.info("memory: " + memory);
      memoryRepository.deleteById(memoryCreated.getId());
      memoryRepository.flush();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  private void validateImage(String imageFile) {
    if (imageFile == null || imageFile.equalsIgnoreCase("")) {
      log.info(
          "validateImage(String imageFile) - Hubo un problema con la imagen, parece estar vacia");
      throw new PlantCareServerErrorException("Es necesaria la imagen de tu planta");
    }
  }
}