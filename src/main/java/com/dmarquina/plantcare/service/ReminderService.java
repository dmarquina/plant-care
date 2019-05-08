package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Reminder;

import java.time.LocalDate;

public interface ReminderService {

  Reminder create(Reminder reminder);

  int updateLastDateAction(Long id,LocalDate newLastActionDate);

  int updatePostponedDays(Long id, Long daysToPostpone);

}
