package com.dmarquina.plantcare.service;

import org.springframework.web.multipart.MultipartFile;

public interface AmazonService {
  String uploadFile(Long plantId, String ownerId, MultipartFile multipartFile);

  void deleteFile(String fileName);
}
