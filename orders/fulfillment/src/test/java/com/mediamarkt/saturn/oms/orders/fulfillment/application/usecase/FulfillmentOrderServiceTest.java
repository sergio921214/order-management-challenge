package com.mediamarkt.saturn.oms.orders.fulfillment.application.usecase;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.Optional;

import com.mediamarkt.saturn.oms.orders.fulfillment.application.usecase.resultgenerator.FulfillmentResultGenerator;
import com.mediamarkt.saturn.oms.statemachine.service.StateMachineService;
import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import com.medimarkt.saturn.oms.domain.repository.OrderFulfillmentRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderReadRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderStateUpdateRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class FulfillmentOrderServiceTest {

  @InjectMocks
  private FulfillmentOrderService fulfillmentOrderService;

  @Mock
  private OrderReadRepository orderReadRepository;

  @Mock
  private OrderStateUpdateRepository orderStateUpdateRepository;

  @Mock
  private OrderFulfillmentRepository orderFulfillmentRepository;

  @Mock
  private StateMachineService stateMachineService;

  @Mock
  private FulfillmentResultGenerator fulfillmentResultGenerator;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void execute_SuccessfulFulfillment_ShouldUpdateStateToClosed() {
    Long orderId = 1L;
    Order order = Order.builder().id(orderId).state(OrderState.IN_FULFILLMENT).build();

    when(orderReadRepository.findById(orderId)).thenReturn(Optional.of(order));
    when(fulfillmentResultGenerator.generate()).thenReturn("success");

    fulfillmentOrderService.execute(orderId);

    verify(orderFulfillmentRepository, times(1)).updateFulfillmentResult(orderId, "success");
    verify(orderStateUpdateRepository, times(1)).updateState(orderId, OrderState.CLOSED.name());
    verify(stateMachineService, times(1)).sendEvent(String.valueOf(orderId), "CLOSE");
  }

  @Test
  void execute_FailedFulfillment_ShouldNotUpdateStateToClosed() {
    Long orderId = 1L;
    Order order = Order.builder().id(orderId).state(OrderState.IN_FULFILLMENT).build();

    when(orderReadRepository.findById(orderId)).thenReturn(Optional.of(order));
    when(fulfillmentResultGenerator.generate()).thenReturn("failure");

    fulfillmentOrderService.execute(orderId);

    verify(orderFulfillmentRepository, times(1)).updateFulfillmentResult(orderId, "failure");
    verify(orderStateUpdateRepository, never()).updateState(anyLong(), anyString());
    verify(stateMachineService, never()).sendEvent(anyString(), anyString());
  }

  @Test
  void execute_OrderNotFound_ShouldThrowException() {
    Long orderId = 1L;

    when(orderReadRepository.findById(orderId)).thenReturn(Optional.empty());

    assertThrows(IllegalStateException.class, () -> fulfillmentOrderService.execute(orderId));
    verifyNoInteractions(orderFulfillmentRepository, orderStateUpdateRepository, stateMachineService);
  }

  @Test
  void execute_OrderNotInFulfillmentState_ShouldThrowException() {
    Long orderId = 1L;
    Order order = Order.builder().id(orderId).state(OrderState.CREATED).build();

    when(orderReadRepository.findById(orderId)).thenReturn(Optional.of(order));

    assertThrows(IllegalStateException.class, () -> fulfillmentOrderService.execute(orderId));
    verifyNoInteractions(orderFulfillmentRepository, orderStateUpdateRepository, stateMachineService);
  }
}
