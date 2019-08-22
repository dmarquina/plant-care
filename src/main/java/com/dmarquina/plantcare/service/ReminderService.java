package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Reminder;

import java.time.LocalDate;
import java.util.List;

public interface ReminderService {

  Reminder create(Reminder reminder);

  void deleteById(long reminderId);

  int updateLastDateAction(List<Long> ids,LocalDate lastActionDate);

  int updatePostponedDays(List<Long> ids, Long daysToPostpone);

}
