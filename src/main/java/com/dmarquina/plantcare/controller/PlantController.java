package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.NewPlantRequest;
import com.dmarquina.plantcare.dto.request.UpdateLastDayWateringRequest;
import com.dmarquina.plantcare.dto.request.UpdatePlantRequest;
import com.dmarquina.plantcare.dto.response.PlantResponse;
import com.dmarquina.plantcare.model.Plant;
import com.dmarquina.plantcare.service.PlantService;
import com.dmarquina.plantcare.util.DateUtil;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api(tags = "Plantas")
@RestController
@RequestMapping("/plantcare")
public class PlantController {

  @Autowired
  PlantService plantService;

  @ApiOperation(value = "Listar plantas",
      notes = "Servicio para listar las plantas")
  @ApiResponses(
      value = { @ApiResponse(code = 201, message = "Plantas listadas correctamente"),
          @ApiResponse(code = 400, message = "Solicitud inválida"),
          @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/user/{userId}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public List<PlantResponse> findAllPlants(@PathVariable String userId) {
    return plantService.findAllMyPlants(userId)
        .stream()
        .map(this::convertPlantToPlantResponse)
        .collect(Collectors.toList());
  }

  private PlantResponse convertPlantToPlantResponse(
      Plant plant) {
    PlantResponse wpr = new PlantResponse();
    BeanUtils.copyProperties(plant, wpr);
    wpr.setDaysSinceLastDayWatering(
        DateUtil.getDaysSinceLastWatering(plant.getLastDayWatering()));
    return wpr;
  }

  @ApiOperation(value = "Obtiene la planta  por id",
      notes = "Servicio para obtener la planta  por id")
  @ApiResponses(
      value = { @ApiResponse(code = 201, message = "Planta  obtenida correctamente"),
          @ApiResponse(code = 400, message = "Solicitud inválida"),
          @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public PlantResponse plantById(@PathVariable Long id) {
    Plant plantFound = plantService.findById(id);
    PlantResponse plantResponse = new PlantResponse();
    BeanUtils.copyProperties(plantFound, plantResponse);
    plantResponse.setDaysSinceLastDayWatering(
        DateUtil.getDaysSinceLastWatering(plantFound.getLastDayWatering()));
    return plantResponse;
  }

  @ApiOperation(value = "Crear planta ",
      notes = "Servicio para crear una nueva planta ")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Planta  creada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public PlantResponse createPlant(
      @RequestBody NewPlantRequest plantRequest) {

    Plant newPlant = new Plant();
    BeanUtils.copyProperties(plantRequest, newPlant);

    Plant plantCreated = plantService.create(newPlant);
    PlantResponse plantResponse = new PlantResponse();
    BeanUtils.copyProperties(plantCreated, plantResponse);
    plantResponse.setDaysSinceLastDayWatering(
        DateUtil.getDaysSinceLastWatering(plantCreated.getLastDayWatering()));

    return plantResponse;
  }

  @ApiOperation(value = "Actualizar una  planta  por id",
      notes = "Servicio para actualizar una planta ")
  @ApiResponses(
      value = { @ApiResponse(code = 201, message = "Planta  actualizada correctamente"),
          @ApiResponse(code = 400, message = "Solicitud inválida"),
          @ApiResponse(code = 500, message = "Error en el servidor") })
  @PutMapping(value = "/", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public PlantResponse updatePlant(
      @RequestBody UpdatePlantRequest updatePlantRequest) {

    Plant updatePlant = new Plant();
    BeanUtils.copyProperties(updatePlantRequest, updatePlant);

    Plant plantUpdated = plantService.update(updatePlant);

    PlantResponse plantResponse = new PlantResponse();
    BeanUtils.copyProperties(plantUpdated, plantResponse);
    plantResponse.setDaysSinceLastDayWatering(
        DateUtil.getDaysSinceLastWatering(plantUpdated.getLastDayWatering()));

    return plantResponse;
  }

  @ApiOperation(value = "Agregar una imagen de planta ",
      notes = "Servicio para agregar una imagen de una planta ")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Imagen agregada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/image", headers = ("Content-Type=multipart/form-data"))
  public PlantResponse uploadPlantImage(@RequestParam() Long id,
      @RequestParam("image") MultipartFile multipartFile) {
    Plant plantUpdated = plantService.updateImagePlant(id, multipartFile);
    PlantResponse plantResponse = new PlantResponse();

    BeanUtils.copyProperties(plantUpdated, plantResponse);
    plantResponse.setDaysSinceLastDayWatering(
        DateUtil.getDaysSinceLastWatering(plantUpdated.getLastDayWatering()));
    return plantResponse;
  }

  @ApiOperation(value = "Actualizar último día de riego",
      notes = "Servicio para Actualizar último día de riego")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Planta actualizada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PatchMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public PlantResponse waterPlant(@PathVariable Long id,
      @RequestBody UpdateLastDayWateringRequest newLastDayWatered) {

    Plant plantPatched =
        plantService.updateLastDayWatering(id, newLastDayWatered.getLastDayWatering());

    PlantResponse plantResponse = new PlantResponse();

    BeanUtils.copyProperties(plantPatched, plantResponse);
    plantResponse.setDaysSinceLastDayWatering(
        DateUtil.getDaysSinceLastWatering(plantPatched.getLastDayWatering()));

    return plantResponse;
  }

}
