package com.medimarkt.saturn.oms.domain.usecase;

import com.medimarkt.saturn.oms.domain.model.Basket;
import com.medimarkt.saturn.oms.domain.model.Order;

public interface CreateOrderUseCase {

  Order execute(Basket basket);
}

