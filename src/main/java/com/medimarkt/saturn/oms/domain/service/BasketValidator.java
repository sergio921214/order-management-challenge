package com.medimarkt.saturn.oms.domain.service;

import com.medimarkt.saturn.oms.domain.model.Basket;
import com.medimarkt.saturn.oms.domain.model.BasketItem;

public class BasketValidator {

  public static void validateBasket(Basket basket) {
    if (basket.getItems() == null || basket.getItems().isEmpty()) {
      throw new IllegalArgumentException("Basket cannot be empty");
    }

    for (BasketItem item : basket.getItems()) {
      if (item.getQuantity() > item.getItem().getStock()) {
        throw new IllegalArgumentException("Item " + item.getItem().getName() + " does not have enough stock");
      }
    }
  }
}
