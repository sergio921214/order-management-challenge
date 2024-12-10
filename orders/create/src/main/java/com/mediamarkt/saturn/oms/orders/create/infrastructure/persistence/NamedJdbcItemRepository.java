package com.mediamarkt.saturn.oms.orders.create.infrastructure.persistence;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.medimarkt.saturn.oms.domain.repository.ItemRepository;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class NamedJdbcItemRepository implements ItemRepository {

  private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  public NamedJdbcItemRepository(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
    this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
  }

  @Override
  public Map<Long, Integer> findStockByIds(List<Long> itemIds) {
    String sql = "SELECT id, stock FROM order_management.items WHERE id IN (:ids)";

    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("ids", itemIds);

    return namedParameterJdbcTemplate.query(sql, parameters, (rs, rowNum) ->
        Map.entry(rs.getLong("id"), rs.getInt("stock"))
    ).stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
  }

  @Override
  public void decreaseStock(Long itemId, int quantity) {
    String sql = "UPDATE order_management.items SET stock = stock - :quantity WHERE id = :itemId AND stock >= :quantity";

    MapSqlParameterSource parameters = new MapSqlParameterSource();
    parameters.addValue("itemId", itemId);
    parameters.addValue("quantity", quantity);

    int rowsAffected = namedParameterJdbcTemplate.update(sql, parameters);

    if (rowsAffected == 0) {
      throw new IllegalArgumentException("Insufficient stock for item with ID: " + itemId);
    }
  }
}
