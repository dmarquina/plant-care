package com.dmarquina.plantcare.service;

import com.dmarquina.plantcare.dto.request.ProductDetailRequest;
import com.dmarquina.plantcare.model.Sale;
import com.dmarquina.plantcare.model.User;

public interface SaleService {

  User makeSale(Sale sale, ProductDetailRequest ProductDetail);

}
