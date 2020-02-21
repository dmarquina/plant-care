package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.MemoryRequest;
import com.dmarquina.plantcare.dto.response.MemoryResponse;
import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.service.MemoryService;

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

@Api(tags = "Recuerdos")
@RestController
@RequestMapping("/memories")
public class MemoryController {

  @Autowired
  MemoryService memoryService;

  @ApiOperation(value = "Listar memorias", notes = "Servicio para listar memorias")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Memorias de plantas listado correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{plantId}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<MemoryResponse>> findMemoryByPlantId(@PathVariable Long plantId) {
    return ResponseEntity.ok(memoryService.findMemoryByPlantId(plantId)
                                 .stream()
                                 .map(MemoryResponse::new)
                                 .collect(Collectors.toList()));
  }

  @ApiOperation(value = "Crear entrada a las memorias", notes = "Servicio para crear entrada al memoria ")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Entrada a las memorias creada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<MemoryResponse> createMemory(@RequestBody @Valid MemoryRequest memoryRequest) {
    Memory newMemory = new Memory();
    BeanUtils.copyProperties(memoryRequest, newMemory);
    Memory memoryCreated = memoryService.create(newMemory);
    MemoryResponse memoryResponse = new MemoryResponse(memoryCreated);
    return ResponseEntity.ok(memoryResponse);
  }
}
