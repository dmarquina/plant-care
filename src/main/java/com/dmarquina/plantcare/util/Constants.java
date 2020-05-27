package com.dmarquina.plantcare.util;

import com.dmarquina.plantcare.model.Subscription;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Constants {

  public static final int MAX_QUANTITY_PLANTS_DEFAULT = 2;
  public static final int MAX_QUANTITY_PLANT_MEMORIES_DEFAULT = 4;
  public static final boolean EMAIL_VERIFIED_FALSE = false;
  public static final boolean EMAIL_VERIFIED_TRUE = true;
  public static final String DEFAULT_VERIFICATION_CODE = "DEF4ULT";

  public static final String CREATE_NEW_PLANT_ACTION = "createPlant";
  public static final String CREATE_NEW_MEMORY_ACTION = "createMemory";

  public static boolean isSubscriptionInActivePeriod(Subscription subscription) {
    if (subscription != null) {
      return LocalDate.now()
          .until(subscription.getEndDate(), ChronoUnit.DAYS) <= 0;
    } else {
      return false;
    }
  }
}
