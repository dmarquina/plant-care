package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.dto.request.UserVerificationCodeRequest;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.AuthService;
import com.dmarquina.plantcare.util.UserEmailVerification;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class AuthServiceImplTest {

  @TestConfiguration
  static class AuthServiceImplTestContextConfiguration {
    @Bean
    public AuthService authService() {
      return new AuthServiceImpl();
    }
  }

  @Autowired
  AuthService authService;

  @MockBean
  UserEmailVerification userEmailVerification;

  @MockBean
  UserRepository userRepository;

  @Before
  public void setUp() {
    User user = new User();
    user.setId("OYE1");
    user.setVerificationCode("CODEVER");
    user.setIsEmailVerified(false);
    Mockito.when(userRepository.findById(user.getId()))
        .thenReturn(Optional.of(user));

    User userEmailVerified = new User();
    userEmailVerified.setId("OYE1");
    userEmailVerified.setVerificationCode("CODEVER");
    userEmailVerified.setIsEmailVerified(true);
    Mockito.when(userRepository.save(userEmailVerified))
        .thenReturn(userEmailVerified);
  }

  @Test(expected = ResourceNotFoundException.class)
  public void userNotFoundInVerificationCode() {
    authService.verifySignUpCode(new UserVerificationCodeRequest("OYE2", "CODEVER"));
  }

  @Test
  @Ignore
  public void verificationCodeIsOk() {
    User userVerified =
        authService.verifySignUpCode(new UserVerificationCodeRequest("OYE1", "CODEVER"));
    Assert.assertTrue(userVerified.getIsEmailVerified());
  }

  @Test(expected = PlantCareServerErrorException.class)
  @Ignore
  public void verificationCodeIsOkButExceptionUpdatingUser() {
    User user = new User();
    user.setId("OYE2");
    user.setVerificationCode("CODEVER");
    user.setIsEmailVerified(false);
    Mockito.when(userRepository.findById(user.getId()))
        .thenReturn(Optional.of(user));

    User userEmailVerified2 = new User();
    userEmailVerified2.setId("OYE2");
    userEmailVerified2.setVerificationCode("CODEVER");
    userEmailVerified2.setIsEmailVerified(true);
    Mockito.when(userRepository.save(userEmailVerified2))
        .thenThrow(PlantCareServerErrorException.class);
    authService.verifySignUpCode(new UserVerificationCodeRequest("OYE2", "CODEVER"));
  }

  @Test(expected = ResourceNotFoundException.class)
  public void verificationCodeIsNotOk() {
    authService.verifySignUpCode(new UserVerificationCodeRequest("OYE1", "CODELOV"));
  }


}