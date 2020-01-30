package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.JournalRequest;
import com.dmarquina.plantcare.dto.request.PlantRequest;
import com.dmarquina.plantcare.dto.response.JournalResponse;
import com.dmarquina.plantcare.dto.response.PlantResponse;
import com.dmarquina.plantcare.model.Journal;
import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.service.JournalService;
import com.dmarquina.plantcare.service.PlantService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Diario")
@RestController
@RequestMapping("/journals")
public class JournalController {

  @Autowired
  JournalService journalService;

  @ApiOperation(value = "Listar diario", notes = "Servicio para listar diario")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Diario de plantas listado correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{plantId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<JournalResponse>> findJournalByPlantId(@PathVariable Long plantId) {
    return ResponseEntity.ok(journalService.findJournalByPlantId(plantId)
                                 .stream()
                                 .map(JournalResponse::new)
                                 .collect(Collectors.toList()));
  }

  @ApiOperation(value = "Crear entrada al diario ", notes = "Servicio para crear entrada al diario ")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Entrada al diario creada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<JournalResponse> createJournal(@RequestBody @Valid JournalRequest journalRequest) {
    Journal newJournal = new Journal();
    BeanUtils.copyProperties(journalRequest, newJournal);
    Journal plantCreated = journalService.create(newJournal);
    JournalResponse plantResponse = new JournalResponse(plantCreated);
    return ResponseEntity.ok(plantResponse);
  }
}
