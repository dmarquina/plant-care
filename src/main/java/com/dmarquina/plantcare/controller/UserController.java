package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.MemoryRequest;
import com.dmarquina.plantcare.dto.request.UserRequest;
import com.dmarquina.plantcare.dto.response.MemoryResponse;
import com.dmarquina.plantcare.dto.response.PlantResponse;
import com.dmarquina.plantcare.dto.response.UserResponse;
import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.service.UserService;

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

@Api(tags = "User")
@RestController
@RequestMapping("/users")
public class UserController {

  @Autowired
  UserService userService;

  @ApiOperation(value = "Crear/Actualizar usuario ", notes = "Servicio para crear un nuevo usuario")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Usuario creada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> createUpdateUser(
      @RequestBody @Valid UserRequest userRequest) {
    User user = new User();
    BeanUtils.copyProperties(userRequest, user);
    User userCreated = userService.createUpdateUser(user);
    UserResponse userResponse = new UserResponse(userCreated);
    return ResponseEntity.ok(userResponse);
  }

  @ApiOperation(value = "Obtener usuario por id",
      notes = "Servicio para obtener un usuario por su id")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Usuario creada correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> getUser(@PathVariable String id) {
    return ResponseEntity.ok(new UserResponse(userService.getUser(id)));
  }


  @ApiOperation(value = "Listar plantas por usuario",
      notes = "Servicio para listar las plantas por usuario")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Plantas listadas correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}/plants", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<List<PlantResponse>> findAllPlantsByUser(@PathVariable String id) {
    return ResponseEntity.ok(userService.findAllMyPlants(id)
                                 .stream()
                                 .map(PlantResponse::new)
                                 .collect(Collectors.toList()));
  }

}
