package com.mediamarkt.saturn.oms.orders.create.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

@ExtendWith(MockitoExtension.class)
public class NamedJdbcItemRepositoryTest {

  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @InjectMocks
  private NamedJdbcItemRepository itemRepository;

  @Test
  public void testFindStockByIds() {
    List<Long> itemIds = List.of(1L, 2L, 3L);
    String sql = "SELECT id, stock FROM order_management.items WHERE id IN (:ids)";

    when(namedParameterJdbcTemplate.query(eq(sql), any(MapSqlParameterSource.class), any(RowMapper.class))).thenReturn(
        List.of(Map.entry(1L, 10), Map.entry(2L, 5), Map.entry(3L, 0)));

    Map<Long, Integer> stockMap = itemRepository.findStockByIds(itemIds);

    assertEquals(3, stockMap.size());
    assertEquals(10, stockMap.get(1L));
    assertEquals(5, stockMap.get(2L));
    assertEquals(0, stockMap.get(3L));
  }

  @Test
  public void testDecreaseStock_Success() {
    String sql = "UPDATE order_management.items SET stock = stock - :quantity WHERE id = :itemId AND stock >= :quantity";
    ArgumentCaptor<MapSqlParameterSource> captor = ArgumentCaptor.forClass(MapSqlParameterSource.class);

    when(namedParameterJdbcTemplate.update(eq(sql), any(MapSqlParameterSource.class))).thenReturn(1);

    itemRepository.decreaseStock(1L, 5);

    verify(namedParameterJdbcTemplate, times(1)).update(eq(sql), captor.capture());

    MapSqlParameterSource capturedParams = captor.getValue();
    assertEquals(1L, capturedParams.getValue("itemId"));
    assertEquals(5, capturedParams.getValue("quantity"));
  }

  @Test
  public void testDecreaseStock_InsufficientStock() {
    String sql = "UPDATE order_management.items SET stock = stock - :quantity WHERE id = :itemId AND stock >= :quantity";

    when(namedParameterJdbcTemplate.update(eq(sql), any(MapSqlParameterSource.class))).thenReturn(0);

    IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
        itemRepository.decreaseStock(1L, 5)
    );

    assertEquals("Insufficient stock for item with ID: 1", exception.getMessage());

    verify(namedParameterJdbcTemplate, times(1)).update(eq(sql), any(MapSqlParameterSource.class));
  }

}
