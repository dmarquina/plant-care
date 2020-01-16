package com.dmarquina.plantcare.service;

import java.io.File;

public interface AmazonService {
  String uploadFile(Long plantId, String ownerId, File file);

  void deleteFile(String fileName);
}
