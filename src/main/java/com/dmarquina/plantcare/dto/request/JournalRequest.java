package com.dmarquina.plantcare.dto.request;

import com.dmarquina.plantcare.util.AWSUtils;

import java.time.LocalDate;
import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class JournalRequest {

  @NotEmpty(message = "Es necesario el id de la planta")
  private String plantId;
  @NotEmpty(message = "Es necesaria una imagen")
  private String image;
  private String status;

  public String getImage() {
    return this.image != null ? AWSUtils.getImageNameFromURL(this.image) : "";
  }
}
