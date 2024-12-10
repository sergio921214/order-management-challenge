package com.mediamarkt.saturn.oms.orders.payment.infrastructure.persistence;

import com.medimarkt.saturn.oms.domain.repository.OrderStateUpdateRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NamedJdbcOrderStateUpdateRepository implements OrderStateUpdateRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public void updateState(Long orderId, String newState) {

    String sql =
        "UPDATE order_management.orders SET state = CAST(:newState AS order_management.order_state), updated_at = NOW() WHERE id = "
            + ":orderId";

    MapSqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("newState", newState)
        .addValue("orderId", orderId);

    int rowsUpdated = namedParameterJdbcTemplate.update(sql, parameters);

    if (rowsUpdated == 0) {
      throw new IllegalStateException("No order found with id: " + orderId);
    }
  }
}
