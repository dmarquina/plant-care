package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.User;

public interface UserService {

  User createUpdateUser(User user);

  User getUser(String id);
}
