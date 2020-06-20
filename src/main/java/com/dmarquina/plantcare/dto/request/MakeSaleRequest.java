package com.dmarquina.plantcare.dto.request;

import lombok.Data;

@Data
public class MakeSaleRequest {
  private String platform;
  private String product;
  private String userId;
  private String currency;
  private Double price;
  private ProductDetailRequest productDetail;

}
