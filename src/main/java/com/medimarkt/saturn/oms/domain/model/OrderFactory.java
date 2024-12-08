package com.medimarkt.saturn.oms.domain.model;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;

public class OrderFactory {
	public static Order createNewOrder() {
		return Order.builder().state(OrderState.CREATED).build();
	}
}
