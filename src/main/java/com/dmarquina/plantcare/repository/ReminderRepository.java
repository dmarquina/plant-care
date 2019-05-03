package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.Reminder;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
//  @Modifying
//  @Query("update Reminder r set r.lastDayAction = :lastDayAction where r.plant = :plantId")
//  int updatePlantLastDayAction(@Param("lastDayAction") LocalDate lastDayAction,
//      @Param("plantId") long plantId);
}
