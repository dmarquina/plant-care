package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.dto.request.UserVerificationCodeRequest;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.util.UserEmailVerification;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class UserServiceImplTest {

  @TestConfiguration
  static class UserServiceImplTestContextConfiguration {
    @Bean
    public UserService userService() {
      return new UserServiceImpl();
    }
  }

  @Autowired
  UserService userService;

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
    userService.verifySignUpCode(new UserVerificationCodeRequest("OYE2", "CODEVER"));
  }

  @Test
  public void verificationCodeIsOk() {
    User userVerified =
        userService.verifySignUpCode(new UserVerificationCodeRequest("OYE1", "CODEVER"));
    Assert.assertTrue(userVerified.getIsEmailVerified());
  }

  @Test(expected = PlantCareServerErrorException.class)
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
    userService.verifySignUpCode(new UserVerificationCodeRequest("OYE2", "CODEVER"));
  }

  @Test(expected = ResourceNotFoundException.class)
  public void verificationCodeIsNotOk() {
    userService.verifySignUpCode(new UserVerificationCodeRequest("OYE1", "CODELOV"));
  }


}