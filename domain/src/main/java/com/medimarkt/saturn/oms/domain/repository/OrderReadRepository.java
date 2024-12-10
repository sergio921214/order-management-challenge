package com.medimarkt.saturn.oms.domain.repository;

import java.util.Optional;

import com.medimarkt.saturn.oms.domain.model.Order;

public interface OrderReadRepository {

  Optional<Order> findById(Long id);

}

