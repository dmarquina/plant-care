package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Reminder;
import com.dmarquina.plantcare.repository.ReminderRepository;
import com.dmarquina.plantcare.service.ReminderService;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("reminderService")
public class ReminderServiceImpl implements ReminderService {

  @Autowired
  private ReminderRepository reminderRepository;

  @Override
  @Transactional
  public Reminder create(Reminder reminder) {
    return reminderRepository.save(reminder);
  }

  @Override
  @Transactional
  public void deleteById(long reminderId) {
    reminderRepository.deleteById(reminderId);
  }

  @Override
  @Transactional
  public int updateLastDateAction(Long id,LocalDate updateLastActionDate) {
    long postponedDays = 0;
    return reminderRepository.updateLastDateAction(id,updateLastActionDate,postponedDays);
  }

  @Override
  @Transactional
  public int updatePostponedDays(Long id, Long daysToPostpone) {
    return reminderRepository.updatePostponedDays(id,daysToPostpone);
  }


}
