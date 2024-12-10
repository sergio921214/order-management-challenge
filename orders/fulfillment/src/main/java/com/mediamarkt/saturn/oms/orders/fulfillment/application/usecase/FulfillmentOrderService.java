package com.mediamarkt.saturn.oms.orders.fulfillment.application.usecase;

import com.mediamarkt.saturn.oms.orders.fulfillment.application.usecase.resultgenerator.FulfillmentResultGenerator;
import com.mediamarkt.saturn.oms.statemachine.service.StateMachineService;
import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import com.medimarkt.saturn.oms.domain.repository.OrderFulfillmentRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderReadRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderStateUpdateRepository;
import com.medimarkt.saturn.oms.domain.usecase.FulfillmentOrderUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class FulfillmentOrderService implements FulfillmentOrderUseCase {

  private final OrderReadRepository orderReadRepository;

  private final OrderStateUpdateRepository orderStateUpdateRepository;

  private final OrderFulfillmentRepository orderFulfillmentRepository;

  private final StateMachineService stateMachineService;

  private final FulfillmentResultGenerator fulfillmentResultGenerator;

  @Override
  @Transactional
  public void execute(Long orderId) {
    Order order = orderReadRepository.findById(orderId)
        .orElseThrow(() -> new IllegalStateException("Order not found with ID: " + orderId));

    if (order.getState() != OrderState.IN_FULFILLMENT) {
      throw new IllegalStateException("Order is not in IN_FULFILLMENT state. Current state: " + order.getState());
    }

    String fulfillmentResult = fulfillmentResultGenerator.generate();

    orderFulfillmentRepository.updateFulfillmentResult(orderId, fulfillmentResult);

    if ("success".equalsIgnoreCase(fulfillmentResult)) {
      orderStateUpdateRepository.updateState(orderId, OrderState.CLOSED.name());
      stateMachineService.sendEvent(String.valueOf(orderId), "CLOSE");
    }
  }
}
