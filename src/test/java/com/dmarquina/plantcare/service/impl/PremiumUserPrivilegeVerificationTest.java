package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.service.factory.PremiumUserServiceImpl;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;

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
public class PremiumUserPrivilegeVerificationTest {

  @TestConfiguration
  static class UserServiceImplTestContextConfiguration {
    @Bean
    public UserService userService() {
      return new PremiumUserServiceImpl();
    }
  }

  @Autowired
  UserService userService;

  @MockBean
  UserRepository userRepository;

  @Before
  public void setUp() {
    User user = new User();
    user.setId("OYE1");
    user.setMaxQuantityPlants(4);
    user.setMaxQuantityPlantMemories(6);
    Mockito.when(userRepository.findById(user.getId()))
        .thenReturn(Optional.of(user));

  }

  @Test(expected = ResourceNotFoundException.class)
  public void userNotFoundInPrivilegeVerification() {
    userService.verifyPrivilege("OYE2", "createPlant");
  }

  @Test
  public void userHaveMorePlantsThanPrivilegeAllows() {
    Mockito.when(userRepository.getPlantsQuantity(Mockito.anyString()))
        .thenReturn(5);
    Assert.assertFalse(userService.verifyPrivilege("OYE1", "createPlant"));
  }

  @Test
  public void userHaveEqualPlantsThanPrivilegeAllows() {
    Mockito.when(userRepository.getPlantsQuantity(Mockito.anyString()))
        .thenReturn(4);
    Assert.assertFalse(userService.verifyPrivilege("OYE1", "createPlant"));
  }

  @Test
  public void userHaveLessPlantsThanPrivilegeAllows() {
    Mockito.when(userRepository.getPlantsQuantity(Mockito.anyString()))
        .thenReturn(3);
    Assert.assertTrue(userService.verifyPrivilege("OYE1", "createPlant"));
  }

  @Test
  public void userHaveMoreMemoriesThanPrivilegeAllows() {
    Mockito.when(userRepository.getMemoriesQuantity(Mockito.anyString()))
        .thenReturn(7);
    Assert.assertFalse(userService.verifyPrivilege("OYE1", "createMemory"));
  }

  @Test
  public void userHaveEqualMemoriesThanPrivilegeAllows() {
    Mockito.when(userRepository.getMemoriesQuantity(Mockito.anyString()))
        .thenReturn(6);
    Assert.assertFalse(userService.verifyPrivilege("OYE1", "createMemory"));
  }

  @Test
  public void userHaveLessMemoriesThanPrivilegeAllows() {
    Mockito.when(userRepository.getMemoriesQuantity(Mockito.anyString()))
        .thenReturn(5);
    Assert.assertTrue(userService.verifyPrivilege("OYE1", "createMemory"));
  }

  @Test(expected = PlantCareServerErrorException.class)
  public void actionDoesntExists() {
    userService.verifyPrivilege("OYE1", "createMemorys");
  }
}