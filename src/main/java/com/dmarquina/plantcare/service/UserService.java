package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.dto.request.UserEmailLoginRequest;
import com.dmarquina.plantcare.dto.request.UserVerificationCodeRequest;
import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.User;

import java.util.List;

public interface UserService {

  User emailSignUp(User user);

  User resendVerificationCode(String id);

  User facebookAuthentication(User user);

  User emailLogin(UserEmailLoginRequest id);

  User verifySignUpCode(UserVerificationCodeRequest userVerificationCodeRequest);

  User getUser(String id);

  List<Plant> findAllMyPlants(String ownerId);

  List<Memory> findMemoriesByOwnerId(String ownerId);

  Boolean verifyPrivilege(String id, String action);

}
