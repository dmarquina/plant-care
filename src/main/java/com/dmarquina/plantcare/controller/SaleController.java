package com.dmarquina.plantcare.controller;

import com.dmarquina.plantcare.dto.request.MakeSaleRequest;
import com.dmarquina.plantcare.dto.request.MemoryRequest;
import com.dmarquina.plantcare.dto.response.MemoryResponse;
import com.dmarquina.plantcare.dto.response.UserResponse;
import com.dmarquina.plantcare.model.Memory;
import com.dmarquina.plantcare.model.Sale;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.service.SaleService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Sales")
@RestController
@RequestMapping("/sales")
public class SaleController {

  @Autowired
  SaleService saleService;

  @ApiOperation(value = "Hacer venta de producto", notes = "Servicio para hacer venta de producto")
  @ApiResponses(
      value = { @ApiResponse(code = 201, message = "Venta de producto realizada correctamente"),
          @ApiResponse(code = 400, message = "Solicitud inválida"),
          @ApiResponse(code = 500, message = "Error en el servidor") })
  @PostMapping(value = "/", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
      produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
  public ResponseEntity<UserResponse> makeSale(
      @RequestBody @Valid MakeSaleRequest makeSaleRequest) {
    Sale sale = new Sale();
    BeanUtils.copyProperties(makeSaleRequest, sale);
    User happyCustomer = saleService.makeSale(sale, makeSaleRequest.getProductDetail());
    UserResponse userResponse = new UserResponse(happyCustomer);
    return ResponseEntity.ok(userResponse);
  }

}
