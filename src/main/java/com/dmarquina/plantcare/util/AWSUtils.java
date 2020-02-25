package com.dmarquina.plantcare.util;

import com.dmarquina.plantcare.util.exceptionhandler.PlantServerErrorException;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.apache.commons.codec.binary.Base64;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class AWSUtils {

  @Resource
  Environment environment;

  public static String CURRENT_PHOTOS_BUCKET_PATH;
  public static String PHOTOS_MEMORIES_BUCKET_PATH;

  public static String CURRENT_PHOTOS_BUCKET;
  public static String PHOTOS_MEMORIES_BUCKET;

  @PostConstruct
  private void initialize() {
    String S3_AKIRASGARDEN_PATH = environment.getProperty("aws_s3_akirasgarden_path");
    String AKIRASGARDEN_BASE_BUCKET = environment.getProperty("aws_akirasgarden_base_bukcet");

    CURRENT_PHOTOS_BUCKET = AKIRASGARDEN_BASE_BUCKET + "current-photos-plants";
    PHOTOS_MEMORIES_BUCKET = AKIRASGARDEN_BASE_BUCKET + "photos-memories";

    CURRENT_PHOTOS_BUCKET_PATH = S3_AKIRASGARDEN_PATH + CURRENT_PHOTOS_BUCKET + "/";
    PHOTOS_MEMORIES_BUCKET_PATH = S3_AKIRASGARDEN_PATH + PHOTOS_MEMORIES_BUCKET + "/";
  }

  public static String getImageURL(String bucketPath, String fileName) {
    return bucketPath + fileName;
  }

  public static String getImageNameOnly(String bucketPath, String imageURL) {
    return imageURL.replace(bucketPath, "");
  }

  public static File createImageFileToUpload(String imageBase64) {
    byte[] decodedBytes = Base64.decodeBase64(imageBase64);
    File imageFile = new File("image");
    try {
      FileOutputStream fos = new FileOutputStream(imageFile);
      fos.write(decodedBytes);
      fos.close();
      return imageFile;
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantServerErrorException("Error con la imagen de la planta.");
    }
  }

  public static String makeFileName(String... params) {
    String fileName;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
    String dateName = dateFormat.format(new Date());
    fileName = String.join("-", params) + "-" + dateName;
    return fileName;
  }

}
