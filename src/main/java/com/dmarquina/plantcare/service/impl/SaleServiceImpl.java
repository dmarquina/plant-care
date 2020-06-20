package com.dmarquina.plantcare.service.impl;

import com.dmarquina.plantcare.dto.request.ProductDetailRequest;
import com.dmarquina.plantcare.model.Sale;
import com.dmarquina.plantcare.model.User;
import com.dmarquina.plantcare.repository.SaleRepository;
import com.dmarquina.plantcare.repository.UserRepository;
import com.dmarquina.plantcare.service.SaleService;
import com.dmarquina.plantcare.util.Messages;
import com.dmarquina.plantcare.util.exceptionhandler.PlantCareServerErrorException;
import com.dmarquina.plantcare.util.exceptionhandler.ResourceNotFoundException;

import java.time.LocalDate;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional(readOnly = true)
@Service("saleService")
public class SaleServiceImpl implements SaleService {

  private final Logger log = LoggerFactory.getLogger(SaleService.class);

  @Autowired
  private SaleRepository saleRepository;

  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public User makeSale(Sale sale, ProductDetailRequest productDetail) {
    sale.setDate(LocalDate.now());
    saleRepository.save(sale);
    User user = findUserById(sale.getUserId());
    int newMaxQuantityPlants = user.getMaxQuantityPlants() + productDetail.getPlantsQuantityToAdd();
    int newMaxQuantityPlantMemories =
        user.getMaxQuantityPlantMemories() + productDetail.getMemoriesQuantityToAdd();
    user.setMaxQuantityPlants(newMaxQuantityPlants);
    user.setMaxQuantityPlantMemories(newMaxQuantityPlantMemories);
    userRepository.save(user);
    return user;
  }

  private User findUserById(String id) {
    try {
      Optional<User> userFoundOptional = userRepository.findById(id);
      if (userFoundOptional.isPresent()) {
        return userFoundOptional.get();
      } else {
        log.info("Error en user ID: " + id);
        throw new ResourceNotFoundException(Messages.USER_NOT_FOUND_EXCEPTION_MESSAGE);
      }
    } catch (Exception e) {
      e.printStackTrace();
      throw new PlantCareServerErrorException(Messages.INTERNAL_SERVER_EXCEPTION_MESSAGE);
    }
  }
}