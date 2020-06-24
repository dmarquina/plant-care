package com.dmarquina.plantcare.util;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

public class UserEmailVerificationTest {

  UserEmailVerification userEmailVerification = new UserEmailVerification();

  @Test()
  public void verificationCodeSixCharactersLength() {
    String verificationCode = userEmailVerification.generateVerificationCode();
    Assert.assertEquals(6, verificationCode.length());
  }

  @Test()
  @Ignore
  public void verificationCodeNotSixCharactersLength() {
    String verificationCode = userEmailVerification.generateVerificationCode();
    Assert.assertNotEquals(Mockito.anyInt(), verificationCode.length());
  }

  @Test()
  public void verificationCodeNotTheSame() {
    String verificationCode1 = userEmailVerification.generateVerificationCode();
    String verificationCode2 = userEmailVerification.generateVerificationCode();
    Assert.assertNotEquals(verificationCode1, verificationCode2);
  }

  @Test()
  public void verificationCodeAllUpperCase() {
    String verificationCode = userEmailVerification.generateVerificationCode();
    Assert.assertEquals(verificationCode, verificationCode.toUpperCase());
  }
}