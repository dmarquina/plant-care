package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.response.GardenPlantResponse;
import com.dmarquina.plantcare.dto.response.MemoryResponse;
import com.dmarquina.plantcare.dto.response.VerifyPrivilegeResponse;
import com.dmarquina.plantcare.service.UserService;
import com.dmarquina.plantcare.service.factory.UserServiceFactory;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "User")
@RestController
@RequestMapping("/users")
public class UserController {

  UserService userService;

  @Autowired
  UserServiceFactory userServiceFactory;

  @ApiOperation(value = "Listar plantas por usuario",
      notes = "Servicio para listar las plantas por usuario")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Plantas listadas correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}/plants", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<GardenPlantResponse> findAllGardenPlantsByUser(@PathVariable String id) {
    userService = userServiceFactory.getUserServiceImpl(id);
    return ResponseEntity.ok(new GardenPlantResponse(userService.findAllMyPlants(id)));
  }

  @ApiOperation(value = "Verificar si el ususario puede realizar determinada acción",
      notes = "Servicio para verificar si el ususario puede realizar determinada acción")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Usuario verificado correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}/verifyprivilege/{action}",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<VerifyPrivilegeResponse> verifyPrivilege(@PathVariable String id,
      @PathVariable String action) {
    userService = userServiceFactory.getUserServiceImpl(id);
    Boolean verifiedUserPrivilege = userService.verifyPrivilege(id, action);
    VerifyPrivilegeResponse privilegeResponse = new VerifyPrivilegeResponse(verifiedUserPrivilege);
    return ResponseEntity.ok(privilegeResponse);
  }

  @ApiOperation(value = "Obtener la cantidad de recuerdos por id de usuario",
      notes = "Servicio para obtener la cantidad de recuerdos por id de usuario")
  @ApiResponses(
      value = { @ApiResponse(code = 201, message = "Cantidad de recuerdos obtenido correctamente"),
          @ApiResponse(code = 400, message = "Solicitud inválida"),
          @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}/memories", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<MemoryResponse>> findMemoriesByOwnerId(@PathVariable String id) {
    userService = userServiceFactory.getUserServiceImpl(id);
    return ResponseEntity.ok(userService.findMemoriesByOwnerId(id)
                                 .stream()
                                 .map(MemoryResponse::new)
                                 .collect(Collectors.toList()));
  }

}