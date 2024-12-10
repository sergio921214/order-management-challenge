package com.mediamarkt.saturn.oms.orders.payment.application.usecase;

import com.mediamarkt.saturn.oms.statemachine.service.StateMachineService;
import com.medimarkt.saturn.oms.domain.repository.OrderStateUpdateRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class ConfirmPaymentService {

  private final OrderStateUpdateRepository orderStateUpdateRepository;

  private final StateMachineService stateMachineService;

  @Transactional
  public void execute(Long orderId) {
    stateMachineService.sendEvent(String.valueOf(orderId), "PAY");

    orderStateUpdateRepository.updateState(orderId, "PAID");
  }
}
