package com.medimarkt.saturn.oms.domain.repository;

import java.util.List;
import java.util.Map;

public interface ItemRepository {

  Map<Long, Integer> findStockByIds(List<Long> itemIds);

  void decreaseStock(Long itemId, int quantity);
}
