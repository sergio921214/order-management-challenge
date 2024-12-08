package com.medimarkt.saturn.oms.domain.usecase;

import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;

public interface UpdateOrderStateUseCase {

  Order execute(Long orderId, OrderState newState);
}
