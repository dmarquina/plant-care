package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Reminder;
import com.dmarquina.plantcare.model.ReminderExecuted;
import com.dmarquina.plantcare.repository.ReminderExecutedRepository;
import com.dmarquina.plantcare.repository.ReminderRepository;
import com.dmarquina.plantcare.service.ReminderService;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("reminderService")
public class ReminderServiceImpl implements ReminderService {

  @Autowired
  private ReminderRepository reminderRepository;

  @Autowired
  private ReminderExecutedRepository reminderExecutedRepository;

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
  public int updateLastDateAction(List<Long> ids, LocalDate lastActionDate) {
    long postponedDays = 0;
    ids.stream()
        .forEach(reminderId -> reminderExecutedRepository.save(
            new ReminderExecuted(reminderId, lastActionDate)));
    return reminderRepository.updateLastDateAction(ids, lastActionDate, postponedDays);
  }

  @Override
  @Transactional
  public void updatePostponedDays(List<Long> ids, Long daysToPostpone) {
    reminderRepository.findAllById(ids)
        .forEach(reminder -> {
          long daysWithOuAction = reminder.getLastDateAction()
              .until(LocalDate.now(), ChronoUnit.DAYS);
          long actualDaysToPostpone =
              (daysWithOuAction - reminder.getFrequencyDays()) + daysToPostpone;
          reminder.setPostponedDays(actualDaysToPostpone);
          reminderRepository.save(reminder);
        });

  }

}

