package com.medimarkt.saturn.oms.domain.repository;

public interface OrderFulfillmentRepository {

  void updateFulfillmentResult(Long orderId, String result);
}
