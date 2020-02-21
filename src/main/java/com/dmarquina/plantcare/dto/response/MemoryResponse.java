package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Memory;

import java.time.LocalDate;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class MemoryResponse {
  private Long id;
  private String plantId;
  private String image;
  private LocalDate date;
  private String status;

  public MemoryResponse(Memory plantMemory) {
    BeanUtils.copyProperties(plantMemory, this);
  }
}
