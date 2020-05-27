package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.SubscriptionRequest;
import com.dmarquina.plantcare.dto.response.SubscriptionResponse;
import com.dmarquina.plantcare.service.SubscriptionService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Subscription")
@RestController
@RequestMapping("/subscriptions")
public class SubscriptionController {

  @Autowired
  SubscriptionService subscriptionService;

  @ApiOperation(value = "Suscribir usuario", notes = "Servicio para suscribir usuario")
  @ApiResponses(value = { @ApiResponse(code = 201, message = "Suscrito correctamente"),
      @ApiResponse(code = 400, message = "Solicitud inválida"),
      @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<SubscriptionResponse> subscribe(
      @RequestBody @Valid SubscriptionRequest subscriptionRequest) {
    return ResponseEntity.ok(
        new SubscriptionResponse(subscriptionService.subscribe(subscriptionRequest)));
  }
}