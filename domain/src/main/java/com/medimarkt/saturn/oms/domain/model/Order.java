package com.medimarkt.saturn.oms.domain.model;

import java.time.LocalDateTime;
import java.util.List;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class Order {

  private Long id;

  private OrderState state;

  private List<BasketItem> items;

  private LocalDateTime createdAt;

  private LocalDateTime updatedAt;

  private String fulfillmentResult;

  public void pay() {
    if (this.state == OrderState.CREATED) {
      this.state = OrderState.PAID;
    }
  }

  public void fulfill(String result) {
    if (this.state == OrderState.PAID) {
      this.state = OrderState.IN_FULFILLMENT;
      this.fulfillmentResult = result;
    }
  }

  public void close() {
    if (this.state == OrderState.IN_FULFILLMENT) {
      this.state = OrderState.CLOSED;
    }
  }
}
