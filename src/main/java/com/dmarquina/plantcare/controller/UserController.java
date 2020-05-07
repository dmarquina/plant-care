package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.UserEmailLoginRequest;
import com.dmarquina.plantcare.dto.request.UserRequest;
import com.dmarquina.plantcare.dto.request.UserVerificationCodeRequest;
import com.dmarquina.plantcare.dto.response.GardenPlantResponse;
import com.dmarquina.plantcare.dto.response.PlantResponse;
import com.dmarquina.plantcare.dto.response.UserResponse;
import com.dmarquina.plantcare.dto.response.VerifyPrivilegeResponse;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
  @PostMapping(value = "/facebookauthentication", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> facebookAuthentication(
      @RequestBody @Valid UserRequest userRequest) {
    User user = new User();
    BeanUtils.copyProperties(userRequest, user);
    User userFacebookLogged = userService.facebookAuthentication(user);
    UserResponse userResponse = new UserResponse(userFacebookLogged);
    return ResponseEntity.ok(userResponse);
  }

  @ApiOperation(value = "Registrarse con Email", notes = "Servicio para registrarse con Email")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Usuario registrado correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/emailsignup", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> emailSignUp(@RequestBody @Valid UserRequest userRequest) {
    User user = new User();
    BeanUtils.copyProperties(userRequest, user);
    User userSignedUp = userService.emailSignUp(user);
    UserResponse userResponse = new UserResponse(userSignedUp);
    return ResponseEntity.ok(userResponse);
  }

  @ApiOperation(value = "Verificar email de usuario a traves del codigo",
      notes = "Servicio para verificar email de usuario a traves del codigo")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Usuario verificado correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/verifysignupcode", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> verifySigunpCode(
      @RequestBody @Valid UserVerificationCodeRequest userVerificationCodeRequest) {
    User verifiedUserEmail = userService.verifySignUpCode(userVerificationCodeRequest);
    UserResponse userResponse = new UserResponse(verifiedUserEmail);
    return ResponseEntity.ok(userResponse);
  }

  @ApiOperation(value = "Registrarse con Email", notes = "Servicio para registrarse con Email")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Usuario registrado correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @GetMapping(value = "/{id}/resendverificationcode",
      consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> resendVerificationCode(@PathVariable String id) {
    User userToVerifiedEmail = userService.resendVerificationCode(id);
    UserResponse userResponse = new UserResponse(userToVerifiedEmail);
    return ResponseEntity.ok(userResponse);
  }

  @ApiOperation(value = "Registrarse con Email", notes = "Servicio para registrarse con Email")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Usuario registrado correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/emaillogin", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> emailLogin(
      @RequestBody @Valid UserEmailLoginRequest userEmailLoginRequest) {
    User userLogged = userService.emailLogin(userEmailLoginRequest);
    UserResponse userResponse = new UserResponse(userLogged);
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
  public ResponseEntity<GardenPlantResponse> findAllGardenPlantsByUser(@PathVariable String id) {
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
    Boolean verifiedUserPrivilege = userService.verifyPrivilege(id, action);
    VerifyPrivilegeResponse privilegeResponse = new VerifyPrivilegeResponse(verifiedUserPrivilege);
    return ResponseEntity.ok(privilegeResponse);
  }
}
