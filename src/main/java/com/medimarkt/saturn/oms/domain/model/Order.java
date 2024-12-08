package com.medimarkt.saturn.oms.domain.model;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
public class Order {
	private OrderState state;

	public Order(OrderState state) {
		this.state = state;
	}

	public void pay() {
		if (this.state == OrderState.CREATED) {
			this.state = OrderState.PAID;
		}
	}
}
