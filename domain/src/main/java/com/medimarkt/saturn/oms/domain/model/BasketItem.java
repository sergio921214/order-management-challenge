package com.medimarkt.saturn.oms.domain.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class BasketItem {

  private final Item item;

  private final int quantity;

  public BasketItem(Item item, int quantity) {
    if (quantity <= 0) {
      throw new IllegalArgumentException("Quantity must be greater than 0");
    }
    this.item = item;
    this.quantity = quantity;
  }
}
