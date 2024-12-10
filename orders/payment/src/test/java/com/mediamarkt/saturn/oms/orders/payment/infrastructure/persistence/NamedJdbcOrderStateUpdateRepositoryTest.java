package com.mediamarkt.saturn.oms.orders.payment.infrastructure.persistence;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

class NamedJdbcOrderStateUpdateRepositoryTest {

  @Mock
  private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @InjectMocks
  private NamedJdbcOrderStateUpdateRepository repository;

  public NamedJdbcOrderStateUpdateRepositoryTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testUpdateState_Success() {
    String sql =
        "UPDATE order_management.orders SET state = CAST(:newState AS order_management.order_state), updated_at = NOW() WHERE id = "
            + ":orderId";

    when(namedParameterJdbcTemplate.update(eq(sql), any(MapSqlParameterSource.class))).thenReturn(1);

    repository.updateState(1L, "PAID");

    verify(namedParameterJdbcTemplate, times(1)).update(eq(sql), any(MapSqlParameterSource.class));
  }

  @Test
  void testUpdateState_OrderNotFound() {
    String sql =
        "UPDATE order_management.orders SET state = CAST(:newState AS order_management.order_state), updated_at = NOW() WHERE id = "
            + ":orderId";

    when(namedParameterJdbcTemplate.update(eq(sql), any(MapSqlParameterSource.class))).thenReturn(0);

    IllegalStateException exception = assertThrows(IllegalStateException.class, () -> repository.updateState(1L, "PAID"));

    assertEquals("No order found with id: 1", exception.getMessage());
    verify(namedParameterJdbcTemplate, times(1)).update(eq(sql), any(MapSqlParameterSource.class));
  }

}
