package com.dmarquina.plantcare.service;

import java.io.File;

public interface AmazonService {
  String uploadFile(String bucketName, String fileName, File file);

  void deleteFile(String bucketName, String fileName);
}
