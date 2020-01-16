package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.PlantRequest;
import com.dmarquina.plantcare.dto.response.PlantAdminResponse;
import com.dmarquina.plantcare.dto.response.PlantResponse;
import com.dmarquina.plantcare.model.Plant;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Plantas")
@RestController
@RequestMapping("/plants")
public class PlantController {

  @Autowired
  PlantService plantService;

  @ApiOperation(value = "Listar plantas", notes = "Servicio para listar las plantas")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Plantas listadas correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<PlantAdminResponse>> findAllPlants() {
    return ResponseEntity.ok(plantService.findAllPlants()
                                 .stream()
                                 .map(PlantAdminResponse::new)
                                 .collect(Collectors.toList()));
  }

  @ApiOperation(value = "Listar plantas por usuario", notes = "Servicio para listar las plantas por usuario")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Plantas listadas correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/users/{ownerId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<PlantResponse>> findAllPlantsByUser(@PathVariable String ownerId) {
    return ResponseEntity.ok(plantService.findAllMyPlants(ownerId)
                                 .stream()
                                 .map(PlantResponse::new)
                                 .collect(Collectors.toList()));
  }


  @ApiOperation(value = "Obtiene la planta  por id",
      notes = "Servicio para obtener la planta  por id")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Planta  obtenida correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<PlantResponse> plantById(@PathVariable Long id) {
    Plant plantFound = plantService.findById(id);
    if (plantFound != null) {
      PlantResponse plantResponse = new PlantResponse(plantFound);
      return ResponseEntity.ok(plantResponse);
    } else {
      return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
  }

  @ApiOperation(value = "Crear planta ", notes = "Servicio para crear una nueva planta ")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Planta  creada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<PlantResponse> createPlant(@RequestBody @Valid PlantRequest plantRequest) {
    Plant newPlant = new Plant();
    BeanUtils.copyProperties(plantRequest, newPlant);
    newPlant.setReminders(plantRequest.getReminders());
    Plant plantCreated = plantService.create(newPlant);
    PlantResponse plantResponse = new PlantResponse(plantCreated);
    return ResponseEntity.ok(plantResponse);
  }

  @ApiOperation(value = "Actualizar una  planta  por id",
      notes = "Servicio para actualizar una planta ")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Planta  actualizada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PutMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<PlantResponse> updatePlant(@RequestBody PlantRequest plantRequest) {
    Plant plantToUpdate = new Plant();
    BeanUtils.copyProperties(plantRequest, plantToUpdate);
    plantToUpdate.setReminders(plantRequest.getReminders());
    Plant plantUpdated = plantService.update(plantToUpdate, plantRequest.getRemindersToDelete());
    PlantResponse plantResponse = new PlantResponse(plantUpdated);
    return ResponseEntity.ok(plantResponse);
  }

  @ApiOperation(value = "Eliminar una  planta  por id",
      notes = "Servicio para eliminar una planta ")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Planta  eliminada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<?> deletePlant(@PathVariable Long id) {
    plantService.delete(id);
    return new ResponseEntity(HttpStatus.OK);
  }
}
