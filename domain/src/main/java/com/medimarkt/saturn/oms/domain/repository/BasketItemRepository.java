package com.medimarkt.saturn.oms.domain.repository;

public interface BasketItemRepository {

  void saveBasketItem(Long orderId, Long itemId, int quantity);
}
