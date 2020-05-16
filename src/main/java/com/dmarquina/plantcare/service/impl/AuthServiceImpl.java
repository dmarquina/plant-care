package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.dto.request.UserEmailLoginRequest;
import com.dmarquina.plantcare.dto.request.UserVerificationCodeRequest;
import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.AuthService;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.util.Constants;
import com.dmarquina.plantcare.util.Messages;
import com.dmarquina.plantcare.util.UserEmailVerification;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("authService")
public class AuthServiceImpl implements AuthService {
  private final Logger log = LoggerFactory.getLogger(AuthService.class);

  @Autowired
  private UserEmailVerification userEmailVerification;

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public User emailSignUp(User user) {
    try {
      user.setMaxQuantityPlants(Constants.MAX_QUANTITY_PLANTS_DEFAULT);
      user.setMaxQuantityPlantMemories(Constants.MAX_QUANTITY_PLANT_MEMORIES_DEFAULT);
      user.setIsEmailVerified(Constants.EMAIL_VERIFIED_FALSE);
      user.setVerificationCode(userEmailVerification.generateVerificationCode());
      userEmailVerification.sendVerificationCodeMail(user);
      return userRepository.save(user);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  @Transactional
  public User resendVerificationCode(String id) {
    try {
      User user = findUserById(id);
      user.setVerificationCode(userEmailVerification.generateVerificationCode());
      userEmailVerification.sendVerificationCodeMail(user);
      return userRepository.save(user);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  @Transactional
  public User facebookAuthentication(User userRequest) {
    try {
      Optional<User> userFoundOptional = userRepository.findById(userRequest.getId());
      return userFoundOptional.map(user -> facebookLogin(user, userRequest))
          .orElseGet(() -> facebookSignUp(userRequest));
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  private User facebookLogin(User userFound, User userRequest) {
    if (userRequest.getDeviceToken() != null && !userRequest.getDeviceToken()
        .equals(userFound.getDeviceToken())) {
      userFound.setDeviceToken(userRequest.getDeviceToken());
    }
    userFound.setLastLoginDate(LocalDate.now());
    try {
      userRepository.save(userFound);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return userFound;
  }

  private User facebookSignUp(User userRequest) {
    userRequest.setMaxQuantityPlants(Constants.MAX_QUANTITY_PLANTS_DEFAULT);
    userRequest.setMaxQuantityPlantMemories(Constants.MAX_QUANTITY_PLANT_MEMORIES_DEFAULT);
    userRequest.setIsEmailVerified(Constants.EMAIL_VERIFIED_TRUE);
    userRequest.setVerificationCode(Constants.DEFAULT_VERIFICATION_CODE);
    userRequest.setLastLoginDate(LocalDate.now());
    try {
      return userRepository.save(userRequest);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  @Transactional
  public User emailLogin(UserEmailLoginRequest userEmailLoginRequest) {
    User userFound = findUserById(userEmailLoginRequest.getId());
    if (userEmailLoginRequest.getDeviceToken() != null && !userEmailLoginRequest.getDeviceToken()
        .equals(userFound.getDeviceToken())) {
      userFound.setDeviceToken(userEmailLoginRequest.getDeviceToken());
    }
    userFound.setLastLoginDate(LocalDate.now());
    try {
      userRepository.save(userFound);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
    return userFound;
  }

  @Override
  @Transactional
  public User getUser(String id) {
    try {
      return findUserById(id);
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

  @Override
  @Transactional
  public User verifySignUpCode(UserVerificationCodeRequest userVerificationCodeRequest) {
    Optional<User> userOptional = userRepository.findById(userVerificationCodeRequest.getId());
    if (userOptional.isPresent()) {
      User userFound = userOptional.get();
      if (userFound.getVerificationCode()
          .equals(userVerificationCodeRequest.getVerificationCode())) {
        userFound.setIsEmailVerified(true);
        userFound.setLastLoginDate(LocalDate.now());
        try {
          return userRepository.save(userFound);
        } catch (Exception e) {
          e.printStackTrace();
          throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
        }
      } else {
        throw new ResourceNotFoundException("Código de verificación incorrecto");
      }
    } else {
      throw new ResourceNotFoundException(Messages.USER_NOT_FOUND_EXCEPTION_MESSAGE);
    }
  }

  private User findUserById(String id) {
    try {
      Optional<User> userFoundOptional = userRepository.findById(id);
      if (userFoundOptional.isPresent()) {
        return userFoundOptional.get();
      } else {
        log.info("Error en user ID: " + id);
        throw new ResourceNotFoundException(Messages.USER_NOT_FOUND_EXCEPTION_MESSAGE);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }

}