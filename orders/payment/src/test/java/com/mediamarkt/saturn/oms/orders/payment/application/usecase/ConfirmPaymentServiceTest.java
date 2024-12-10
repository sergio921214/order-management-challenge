package com.mediamarkt.saturn.oms.orders.payment.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import com.mediamarkt.saturn.oms.statemachine.service.StateMachineService;
import com.medimarkt.saturn.oms.domain.repository.OrderStateUpdateRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class ConfirmPaymentServiceTest {

  @Mock
  private OrderStateUpdateRepository orderStateUpdateRepository;

  @Mock
  private StateMachineService stateMachineService;

  @InjectMocks
  private ConfirmPaymentService confirmPaymentService;

  public ConfirmPaymentServiceTest() {
    MockitoAnnotations.openMocks(this);
  }

  @Test
  void testExecute_Success() {
    Long orderId = 1L;

    doNothing().when(stateMachineService).sendEvent(String.valueOf(orderId), "PAY");
    doNothing().when(orderStateUpdateRepository).updateState(orderId, "PAID");

    confirmPaymentService.execute(orderId);

    verify(stateMachineService, times(1)).sendEvent(String.valueOf(orderId), "PAY");
    verify(orderStateUpdateRepository, times(1)).updateState(orderId, "PAID");
  }

  @Test
  void testExecute_StateMachineError() {
    Long orderId = 1L;
    doThrow(new RuntimeException("State transition failed"))
        .when(stateMachineService).sendEvent(String.valueOf(orderId), "PAY");

    RuntimeException exception = assertThrows(RuntimeException.class,
        () -> confirmPaymentService.execute(orderId));

    assertEquals("State transition failed", exception.getMessage());
    verify(stateMachineService, times(1)).sendEvent(String.valueOf(orderId), "PAY");
    verifyNoInteractions(orderStateUpdateRepository);
  }

}
