package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.util.AWSUtils;

import java.time.LocalDate;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class MemoryResponse {
  private Long id;
  private Long plantId;
  private String image;
  private LocalDate date;
  private Double status;

  public MemoryResponse(Memory plantMemory) {
    BeanUtils.copyProperties(plantMemory, this);
  }

  public String getImage() {
    return AWSUtils.getImageURL(AWSUtils.PHOTOS_MEMORIES_BUCKET_PATH, image);
  }
}
