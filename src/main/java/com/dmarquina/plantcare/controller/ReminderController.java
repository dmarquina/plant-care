package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.UpdateLastDateAction;
import com.dmarquina.plantcare.dto.request.UpdatePostponedDays;
import com.dmarquina.plantcare.service.ReminderService;

import io.swagger.annotations.Api;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Recordatorios")
@RestController
@RequestMapping("/reminders")
public class ReminderController {

  @Autowired
  ReminderService reminderService;

  @PatchMapping("/{id}/lastdateaction")
  public ResponseEntity<?> updateLastDateAction(@PathVariable("id") Long id,
      @RequestBody UpdateLastDateAction updateLastDateAction) {
    reminderService.updateLastDateAction(id, updateLastDateAction.getLastDateAction());
    return ResponseEntity.ok("resource address updated");
  }

  @PatchMapping("/{id}/postponeddays")
  public ResponseEntity<?> updatePostponedDays(@PathVariable("id") Long id,
      @RequestBody UpdatePostponedDays updatePostponedDays) {
    reminderService.updatePostponedDays(id, updatePostponedDays.getPostponedDays());
    return ResponseEntity.ok("resource address updated");
  }
}
