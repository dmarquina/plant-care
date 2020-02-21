package com.dmarquina.plantcare.service.impl;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.dmarquina.plantcare.service.AmazonService;
import com.dmarquina.plantcare.util.AWSUtils;

import java.io.File;
import javax.annotation.PostConstruct;
import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

@Service
public class AmazonServiceImpl implements AmazonService {

  private final Logger log = LoggerFactory.getLogger(AmazonService.class);

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
  public String uploadFile(String bucketName, String fileName, File file) {
    log.info("Llamando al servicio AWS S3");
    amazonS3.putObject(new PutObjectRequest(bucketName, fileName, file).withCannedAcl(
        CannedAccessControlList.PublicRead));
    log.info("Terminando llamado al servicio AWS S3");
    file.delete();
    return fileName;
  }

  @Override
  public void deleteFile(String bucketName, String fileName) {
    amazonS3.deleteObject(bucketName, fileName);
  }

}
