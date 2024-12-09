package com.mediamarkt.saturn.oms.statemachine.config;

import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.config.EnableStateMachine;
import org.springframework.statemachine.config.StateMachineConfigurerAdapter;
import org.springframework.statemachine.config.builders.StateMachineStateConfigurer;
import org.springframework.statemachine.config.builders.StateMachineTransitionConfigurer;

@Configuration
@EnableStateMachine
public class OrderStateMachineConfig extends StateMachineConfigurerAdapter<OrderState, String> {

  @Override
  public void configure(StateMachineStateConfigurer<OrderState, String> states) throws Exception {
    states
        .withStates()
        .initial(OrderState.CREATED)
        .state(OrderState.PAID)
        .state(OrderState.IN_FULFILLMENT)
        .end(OrderState.CLOSED);
  }

  @Override
  public void configure(StateMachineTransitionConfigurer<OrderState, String> transitions) throws Exception {
    transitions
        .withExternal().source(OrderState.CREATED).target(OrderState.PAID).event("PAY")
        .and()
        .withExternal().source(OrderState.PAID).target(OrderState.IN_FULFILLMENT).event("FULFILL")
        .and()
        .withExternal().source(OrderState.IN_FULFILLMENT).target(OrderState.CLOSED).event("CLOSE");
  }
}

