package com.medimarkt.saturn.oms.domain.model;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import com.medimarkt.saturn.oms.domain.service.BasketValidator;

public class OrderFactory {

  public static Order createNewOrder(Basket basket) {
    BasketValidator.validateBasket(basket);
    return Order.builder().state(OrderState.CREATED).build();
  }
}
