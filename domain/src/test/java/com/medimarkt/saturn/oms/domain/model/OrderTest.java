package com.medimarkt.saturn.oms.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void newOrderShouldBeInCreatedState() {
    Order order = OrderFactory.createNewOrder();
    assertEquals(OrderState.CREATED, order.getState());
  }

  @Test
  void shouldTransitionFromCreatedToPaid() {
    Order order = OrderFactory.createNewOrder();
    order.pay();
    assertEquals(OrderState.PAID, order.getState());
  }
}
