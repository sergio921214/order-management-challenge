package com.medimarkt.saturn.oms.domain.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import org.junit.jupiter.api.Test;

class OrderTest {

  @Test
  void newOrderShouldBeInCreatedState() {
    Item item = Item.builder().id(1L).name("item").price(BigDecimal.valueOf(1.50)).stock(2).build();
    List<BasketItem> basketItems = Collections.singletonList(BasketItem.builder().item(item).quantity(1).build());
    Basket basket = Basket.builder().items(basketItems).build();
    Order order = OrderFactory.createNewOrder(basket);
    assertEquals(OrderState.CREATED, order.getState());
  }

  @Test
  void shouldTransitionFromCreatedToPaid() {
    Item item = Item.builder().id(1L).name("item").price(BigDecimal.valueOf(1.50)).stock(2).build();
    List<BasketItem> basketItems = Collections.singletonList(BasketItem.builder().item(item).quantity(1).build());
    Basket basket = Basket.builder().items(basketItems).build();
    Order order = OrderFactory.createNewOrder(basket);
    order.pay();
    assertEquals(OrderState.PAID, order.getState());
  }
}
