package com.medimarkt.saturn.oms.domain.service;

import java.util.Map;

import com.medimarkt.saturn.oms.domain.model.Basket;
import com.medimarkt.saturn.oms.domain.model.BasketItem;

public class BasketValidator {

  public static void validateNonEmptyBasket(Basket basket) {
    if (basket.getItems() == null || basket.getItems().isEmpty()) {
      throw new IllegalArgumentException("Basket cannot be empty");
    }
  }

  public static void validateBasketStock(Basket basket, Map<Long, Integer> availableStock) {
    for (BasketItem basketItem : basket.getItems()) {
      Long itemId = basketItem.getItem().getId();
      int stock = availableStock.getOrDefault(itemId, 0);

      if (basketItem.getQuantity() > stock) {
        throw new IllegalStateException("Item with ID " + itemId
            + " does not have enough stock. Available: " + stock);
      }
    }
  }

}
