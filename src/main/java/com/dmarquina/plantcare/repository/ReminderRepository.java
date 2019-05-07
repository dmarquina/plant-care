package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Reminder;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
  @Modifying
  @Query("update Reminder r set r.lastDateAction = :lastDateAction where r.id = :reminderId")
  int updateLastDateAction(@Param("reminderId") long reminderId,
      @Param("lastDateAction") LocalDate lastDateAction);

  @Modifying
  @Query("update Reminder r set r.postponedDays = :postponedDays where r.id = :reminderId")
  int updatePostponedDays(@Param("reminderId") long reminderId,
      @Param("postponedDays") long postponedDays);
}
