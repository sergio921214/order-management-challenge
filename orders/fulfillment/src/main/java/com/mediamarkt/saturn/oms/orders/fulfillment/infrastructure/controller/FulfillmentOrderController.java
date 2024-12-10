package com.mediamarkt.saturn.oms.orders.fulfillment.infrastructure.controller;

import com.medimarkt.saturn.oms.domain.usecase.FulfillmentOrderUseCase;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class FulfillmentOrderController {

  private final FulfillmentOrderUseCase fulfillmentOrderUseCase;

  @PostMapping("/{orderId}/fulfill")
  @ResponseStatus(HttpStatus.OK)
  public void fulfillOrder(@PathVariable Long orderId) {
    fulfillmentOrderUseCase.execute(orderId);
  }
}
