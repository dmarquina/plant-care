package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.model.Journal;
import com.dmarquina.plantcare.repository.JournalRepository;
import com.dmarquina.plantcare.service.JournalService;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("journalService")
public class JournalServiceImpl implements JournalService {

  private final Logger log = LoggerFactory.getLogger(JournalService.class);

  @Autowired
  private JournalRepository journalRepository;

  @Override
  public List<Journal> findJournalByPlantId(Long plantId) {
    try {
      log.info("journal for plantId: " + plantId);
      return journalRepository.findByPlantId(plantId);
    } catch (Exception e) {
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public Journal create(Journal journal) {
    try {
      return journalRepository.save(journal);
    } catch (Exception e) {
      log.info("journal: " + journal);
      e.printStackTrace();
      throw e;
    }
  }
}