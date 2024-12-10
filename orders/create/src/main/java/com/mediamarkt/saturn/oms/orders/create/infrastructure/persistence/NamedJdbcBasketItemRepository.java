package com.mediamarkt.saturn.oms.orders.create.infrastructure.persistence;

import java.util.Map;

import com.medimarkt.saturn.oms.domain.repository.BasketItemRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NamedJdbcBasketItemRepository implements BasketItemRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public void saveBasketItem(Long orderId, Long itemId, int quantity) {
    String sql = "INSERT INTO order_management.basket_items (order_id, item_id, quantity) VALUES (:orderId, :itemId, :quantity)";

    Map<String, Object> parameters = Map.of(
        "orderId", orderId,
        "itemId", itemId,
        "quantity", quantity
    );

    namedParameterJdbcTemplate.update(sql, parameters);
  }
}

