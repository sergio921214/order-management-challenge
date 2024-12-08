package com.medimarkt.saturn.oms.domain.repository;

import com.medimarkt.saturn.oms.domain.model.Order;

public interface OrderWriteRepository {

  Order save(Order order);
}
