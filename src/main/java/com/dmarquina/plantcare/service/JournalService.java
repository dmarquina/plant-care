package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.model.Journal;
import com.dmarquina.plantcare.model.Plant;

import java.util.List;

public interface JournalService {

  List<Journal> findJournalByPlantId(Long plantId);

  Journal create(Journal journal);
}
