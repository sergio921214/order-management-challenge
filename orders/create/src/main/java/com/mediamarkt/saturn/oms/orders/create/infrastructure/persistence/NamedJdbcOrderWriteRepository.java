package com.mediamarkt.saturn.oms.orders.create.infrastructure.persistence;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.repository.OrderWriteRepository;
import lombok.AllArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class NamedJdbcOrderWriteRepository implements OrderWriteRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Override
  public Order save(Order order) {
    String sql = "INSERT INTO order_management.orders (state, created_at, updated_at) " +
        "VALUES (CAST(:state AS order_management.order_state), :createdAt, :updatedAt)";

    LocalDateTime now = LocalDateTime.now();

    MapSqlParameterSource parameters = new MapSqlParameterSource()
        .addValue("state", order.getState().name())
        .addValue("createdAt", Timestamp.valueOf(now))
        .addValue("updatedAt", Timestamp.valueOf(now));

    GeneratedKeyHolder keyHolder = new GeneratedKeyHolder();

    namedParameterJdbcTemplate.update(sql, parameters, keyHolder, new String[]{"id"});

    Long generatedId = keyHolder.getKey() != null ? keyHolder.getKey().longValue() : null;

    if (generatedId == null) {
      throw new IllegalStateException("Failed to save the order");
    }

    return Order.builder()
        .id(generatedId)
        .state(order.getState())
        .items(order.getItems())
        .createdAt(order.getCreatedAt())
        .updatedAt(order.getUpdatedAt())
        .build();
  }
}

