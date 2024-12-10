package com.medimarkt.saturn.oms.domain.repository;

public interface OrderStateUpdateRepository {

  void updateState(Long orderId, String newState);
}
