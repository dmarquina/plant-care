package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.dto.request.UserEmailLoginRequest;
import com.dmarquina.plantcare.dto.request.UserVerificationCodeRequest;
import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.User;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

public interface UserService {

  User emailSignUp(User user);

  User resendVerificationCode(String id);

  User facebookAuthentication(User user);

  List<Plant> findAllMyPlants(String ownerId);

  User emailLogin(UserEmailLoginRequest id);

  User getUser(String id);

  User verifySignUpCode(UserVerificationCodeRequest userVerificationCodeRequest);

  Boolean verifyPrivilege(String id, String action);
}
