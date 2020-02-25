package com.dmarquina.plantcare.dto.request;

import com.dmarquina.plantcare.util.AWSUtils;

import javax.validation.constraints.NotEmpty;

import lombok.Data;

@Data
public class MemoryRequest {

  private long plantId;
  @NotEmpty(message = "Es necesaria una imagen")
  private String image;
  private Double status;

  public String getImage() {
    return this.image != null ? AWSUtils.getImageNameOnly(AWSUtils.PHOTOS_MEMORIES_BUCKET_PATH, this.image) : "";
  }
}
