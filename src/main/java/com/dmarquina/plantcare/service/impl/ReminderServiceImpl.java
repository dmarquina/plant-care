package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Reminder;
import com.dmarquina.plantcare.repository.ReminderRepository;
import com.dmarquina.plantcare.service.ReminderService;

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

}
