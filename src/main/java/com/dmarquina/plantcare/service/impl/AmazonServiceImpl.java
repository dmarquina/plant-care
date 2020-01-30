package com.dmarquina.plantcare.service.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.util.AWSUtils;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class AmazonServiceImpl implements AmazonService {

  @Resource
  Environment environment;

  private AmazonS3 amazonS3;

  @PostConstruct
  private void initializeAmazon() {
    this.amazonS3 = new AmazonS3Client(
        new BasicAWSCredentials(environment.getProperty("aws_access_key"),
                                environment.getProperty("aws_secret_key")));
  }

  @Override
  public String uploadFile(Long plantId, String ownerId, File file) {
    String fileName = makeFileName(plantId, ownerId);

    amazonS3.putObject(
        new PutObjectRequest(AWSUtils.AWS_BUCKET_NAME, fileName, file).withCannedAcl(
            CannedAccessControlList.PublicRead));
    file.delete();
    return fileName;
  }

  @Override
  public void deleteFile(String fileName) {
    amazonS3.deleteObject(AWSUtils.AWS_BUCKET_NAME, fileName);
  }

  private String makeFileName(Long plantId, String ownerId) {
    String fileName;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-HH-mm-ss");
    String dateName = dateFormat.format(new Date());
    fileName = ownerId + "-" + plantId.toString() + "-" + dateName;
    return fileName;
  }

}
