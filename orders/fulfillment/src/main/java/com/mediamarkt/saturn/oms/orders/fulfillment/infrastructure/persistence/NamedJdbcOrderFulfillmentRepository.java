package com.mediamarkt.saturn.oms.orders.fulfillment.infrastructure.persistence;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import com.medimarkt.saturn.oms.domain.repository.OrderFulfillmentRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderReadRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderStateUpdateRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NamedJdbcOrderFulfillmentRepository implements OrderFulfillmentRepository, OrderStateUpdateRepository, OrderReadRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public void updateFulfillmentResult(Long orderId, String result) {
    String sql = "UPDATE order_management.orders SET fulfillment_result = :result, updated_at = NOW() WHERE id = :orderId";

    MapSqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("result", result)
        .addValue("orderId", orderId);

    int rowsUpdated = namedParameterJdbcTemplate.update(sql, parameters);

    if (rowsUpdated == 0) {
      throw new IllegalStateException("No order found with id: " + orderId);
    }
  }

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

  @Override
  public Optional<Order> findById(Long id) {
    String sql = "SELECT id, state, created_at, updated_at, fulfillment_result FROM order_management.orders WHERE id = :id";

    MapSqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("id", id);

    return namedParameterJdbcTemplate.query(sql, parameters, rs -> {
      if (rs.next()) {
        return Optional.of(mapRowToOrder(rs));
      } else {
        return Optional.empty();
      }
    });
  }

  private Order mapRowToOrder(ResultSet rs) throws SQLException {
    return Order.builder()
        .id(rs.getLong("id"))
        .state(OrderState.valueOf(rs.getString("state")))
        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
        .updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
        .fulfillmentResult(rs.getString("fulfillment_result"))
        .build();
  }
}
