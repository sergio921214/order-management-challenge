package com.mediamarkt.saturn.oms.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.mediamarkt.saturn.oms.statemachine.StateMachineApplication;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachine;
import org.springframework.test.annotation.DirtiesContext;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@SpringBootTest(classes = StateMachineApplication.class)
public class StateMachineConfigTest {

  @Autowired
  private StateMachine<OrderState, String> stateMachine;

  @Test
  public void testInitialState() {
    stateMachine.start();
    assertEquals(OrderState.CREATED, stateMachine.getState().getId());
    stateMachine.stop();
  }

  @Test
  public void testTransitions() {
    stateMachine.start();

    stateMachine.sendEvent("PAY");
    assertEquals(OrderState.PAID, stateMachine.getState().getId());

    stateMachine.sendEvent("FULFILL");
    assertEquals(OrderState.IN_FULFILLMENT, stateMachine.getState().getId());

    stateMachine.sendEvent("CLOSE");
    assertEquals(OrderState.CLOSED, stateMachine.getState().getId());

    stateMachine.stop();
  }
}
