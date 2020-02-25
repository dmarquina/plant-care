package com.dmarquina.plantcare.repository;

import com.dmarquina.plantcare.model.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<User, String> {
  //  @Query(
  //      "SELECT DISTINCT p FROM Plant p JOIN FETCH p.reminders WHERE p.ownerId= :ownerId ORDER BY p.id DESC")
  //  List<Plant> getAllPlantsAndRemindersByOwnerIdOrderByIdDesc(@Param("ownerId") String ownerId);

  @Query("SELECT u FROM User u INNER JOIN Plant p ON p.ownerId = u.id WHERE p.id = :plantId")
  User getUserByPlantId(@Param("plantId") long plantId);
}
