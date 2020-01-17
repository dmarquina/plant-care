package com.dmarquina.plantcare.util;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AWSUtils {

  @Resource
  Environment environment;

  public static String AWS_CURRENT_PHOTOS_BUCKET;

  @PostConstruct
  private void initialize() {
    AWS_CURRENT_PHOTOS_BUCKET =
        environment.getProperty("aws_akirasgarden_bucket") + "/" + "current-photos-plants";
  }

  public static String getImageURL(String fileName) {
    return AWS_CURRENT_PHOTOS_BUCKET + "/" + fileName;
  }

  public static String getImageNameFromURL(String imageURL) {
    return imageURL.replace(AWS_CURRENT_PHOTOS_BUCKET + "/", "");
  }

}
