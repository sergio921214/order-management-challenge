package com.medimarkt.saturn.oms.domain.model;

import java.util.List;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Basket {

  private final List<BasketItem> items;

  public Basket(List<BasketItem> items) {
    this.items = items;
  }
}
