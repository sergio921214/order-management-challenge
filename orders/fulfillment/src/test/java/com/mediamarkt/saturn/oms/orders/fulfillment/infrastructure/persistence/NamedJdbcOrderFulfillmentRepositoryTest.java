package com.mediamarkt.saturn.oms.orders.fulfillment.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class NamedJdbcOrderFulfillmentRepositoryTest {

  private final NamedParameterJdbcTemplate jdbcTemplate = mock(NamedParameterJdbcTemplate.class);

  private final NamedJdbcOrderFulfillmentRepository repository = new NamedJdbcOrderFulfillmentRepository(jdbcTemplate);

  @Test
  void updateFulfillmentResult_ShouldUpdateResult() {
    String sql = "UPDATE order_management.orders SET fulfillment_result = :result, updated_at = NOW() WHERE id = :orderId";

    when(jdbcTemplate.update(eq(sql), any(MapSqlParameterSource.class))).thenReturn(1);

    repository.updateFulfillmentResult(1L, "Success");

    ArgumentCaptor<MapSqlParameterSource> captor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
    verify(jdbcTemplate).update(eq(sql), captor.capture());

    MapSqlParameterSource params = captor.getValue();
    assertEquals("Success", params.getValue("result"));
    assertEquals(1L, params.getValue("orderId"));
  }

  @Test
  void updateFulfillmentResult_OrderNotFound_ShouldThrowException() {
    String sql = "UPDATE order_management.orders SET fulfillment_result = :result, updated_at = NOW() WHERE id = :orderId";

    when(jdbcTemplate.update(eq(sql), any(MapSqlParameterSource.class))).thenReturn(0);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> repository.updateFulfillmentResult(1L, "Success"));

    assertEquals("No order found with id: 1", exception.getMessage());
  }

  @Test
  void updateState_ShouldUpdateState() {
    String sql =
        "UPDATE order_management.orders SET state = CAST(:newState AS order_management.order_state), updated_at = NOW() WHERE id = "
            + ":orderId";

    when(jdbcTemplate.update(eq(sql), any(MapSqlParameterSource.class))).thenReturn(1);

    repository.updateState(1L, "CLOSED");

    ArgumentCaptor<MapSqlParameterSource> captor = ArgumentCaptor.forClass(MapSqlParameterSource.class);
    verify(jdbcTemplate).update(eq(sql), captor.capture());

    MapSqlParameterSource params = captor.getValue();
    assertEquals("CLOSED", params.getValue("newState"));
    assertEquals(1L, params.getValue("orderId"));
  }
}
