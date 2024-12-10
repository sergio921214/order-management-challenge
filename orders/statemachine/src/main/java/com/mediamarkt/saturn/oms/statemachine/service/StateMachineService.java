package com.mediamarkt.saturn.oms.statemachine.service;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import lombok.AllArgsConstructor;
import org.springframework.statemachine.StateMachine;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class StateMachineService {

  private final StateMachine<OrderState, String> stateMachine;

  private final StateMachineRuntimePersister<OrderState, String, String> persister;

  public void saveInitialState(String machineId, OrderState initialState) {
    try {
      StateMachineContext<OrderState, String> context = new DefaultStateMachineContext<>(
          initialState, null, null, null);
      persister.write(context, machineId);
    } catch (Exception e) {
      throw new RuntimeException("Error saving initial state: " + e.getMessage(), e);
    }
  }

  public void sendEvent(String machineId, String event) {
    try {
      stateMachine.start();
      boolean transitionSuccess = stateMachine.sendEvent(event);
      if (!transitionSuccess) {
        throw new IllegalStateException("It was not possible to get the transition to state: " + event);
      }
      StateMachineContext<OrderState, String> context = new DefaultStateMachineContext<>(
          stateMachine.getState().getId(), null, null, null);
      persister.write(context, machineId);
    } catch (Exception e) {
      throw new RuntimeException("Error processing transaction: " + e.getMessage(), e);
    }
  }

}

