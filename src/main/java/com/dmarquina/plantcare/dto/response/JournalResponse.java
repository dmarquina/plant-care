package com.dmarquina.plantcare.dto.response;

import com.dmarquina.plantcare.model.Journal;

import java.time.LocalDate;

import lombok.Data;

import org.springframework.beans.BeanUtils;

@Data
public class JournalResponse {
  private Long id;
  private String plantId;
  private String image;
  private LocalDate date;
  private String status;

  public JournalResponse(Journal plantJournal) {
    BeanUtils.copyProperties(plantJournal, this);
  }
}
