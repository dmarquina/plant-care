package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.model.User;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, Long> {
//  @Query(
//      "SELECT DISTINCT p FROM Plant p JOIN FETCH p.reminders WHERE p.ownerId= :ownerId ORDER BY p.id DESC")
//  List<Plant> getAllPlantsAndRemindersByOwnerIdOrderByIdDesc(@Param("ownerId") String ownerId);

}
