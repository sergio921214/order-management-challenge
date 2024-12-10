package com.mediamarkt.saturn.oms.orders.create.infrastructure.persistence;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class NamedJdbcBasketItemRepositoryTest {

  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @InjectMocks
  private NamedJdbcBasketItemRepository basketItemRepository;

  @Test
  public void testSaveBasketItem() {
    String sql = "INSERT INTO order_management.basket_items (order_id, item_id, quantity) VALUES (:orderId, :itemId, :quantity)";
    Map<String, Object> parameters = Map.of("orderId", 1L, "itemId", 2L, "quantity", 3);

    basketItemRepository.saveBasketItem(1L, 2L, 3);

    verify(namedParameterJdbcTemplate, times(1)).update(eq(sql), eq(parameters));
  }
}

