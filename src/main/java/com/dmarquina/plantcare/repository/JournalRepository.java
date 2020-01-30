package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Journal;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface JournalRepository extends JpaRepository<Journal, Long> {

  List<Journal> findByPlantId(Long plantId);
}
