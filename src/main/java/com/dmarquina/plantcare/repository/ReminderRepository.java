package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Reminder;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReminderRepository extends JpaRepository<Reminder, Long> {
  @Modifying
  @Query(
      "UPDATE Reminder r SET r.lastDateAction = :lastDateAction, r.postponedDays = :postponedDays WHERE r.id IN :ids")
  int updateLastDateAction(@Param("ids") Collection<Long> ids,
      @Param("lastDateAction") LocalDate lastDateAction,
      @Param("postponedDays") Long postponedDays);

  @Modifying
  @Query("UPDATE Reminder r SET r.postponedDays = :postponedDays WHERE r.id IN :ids")
  int updatePostponedDays(@Param("ids") Collection<Long> ids,
      @Param("postponedDays") Long postponedDays);
}
