package com.mediamarkt.saturn.oms.orders.create.infrastructure.controller;

import com.mediamarkt.saturn.oms.orders.create.infrastructure.mapper.CreateOrderRequestMapper;
import com.mediamarkt.saturn.oms.orders.create.infrastructure.mapper.CreateOrderResponseMapper;
import com.medimarkt.saturn.oms.domain.usecase.CreateOrderUseCase;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.openapitools.model.BasketDTO;
import org.openapitools.model.OrderDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CreateOrderController {

  private final CreateOrderUseCase createOrderUseCase;

  private final CreateOrderRequestMapper requestMapper;

  private final CreateOrderResponseMapper responseMapper;

  @PostMapping("/orders")
  public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody BasketDTO basketDTO) {
    var basket = requestMapper.mapToBasket(basketDTO);
    var order = createOrderUseCase.execute(basket);
    var orderDTO = responseMapper.mapToOrderDTO(order);
    return ResponseEntity.ok(orderDTO);
  }
}
