package com.dmarquina.plantcare.service;

import java.io.File;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonService {
  String uploadFile(Long plantId, String ownerId, MultipartFile multipartFile);
  String uploadFile2(Long plantId, String ownerId, File file);
  void deleteFile(String fileName);
}
