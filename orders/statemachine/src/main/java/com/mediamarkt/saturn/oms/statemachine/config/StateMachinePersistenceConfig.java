package com.mediamarkt.saturn.oms.statemachine.config;

import java.time.LocalDateTime;

import com.mediamarkt.saturn.oms.statemachine.entity.StateMachineContextEntity;
import com.mediamarkt.saturn.oms.statemachine.repository.StateMachineContextRepository;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;
import org.springframework.statemachine.support.StateMachineInterceptor;
import org.springframework.statemachine.support.StateMachineInterceptorAdapter;

@Configuration
@AllArgsConstructor
public class StateMachinePersistenceConfig {

  private final StateMachineContextRepository stateMachineContextRepository;

  @Bean
  public StateMachineRuntimePersister<OrderState, String, String> stateMachineRuntimePersister() {
    return new StateMachineRuntimePersister<>() {
      @Override
      public void write(StateMachineContext<OrderState, String> context, String machineId) {
        StateMachineContextEntity entity =
            StateMachineContextEntity.builder().state(context.getState().name()).machineId(machineId).lastUpdated(LocalDateTime.now())
                .build();
        stateMachineContextRepository.save(entity);
      }

      @Override
      public StateMachineContext<OrderState, String> read(String machineId) {
        return stateMachineContextRepository.findById(machineId)
            .map(entity -> new DefaultStateMachineContext<OrderState, String>(OrderState.valueOf(entity.getState()), null, null, null))
            .orElse(null);
      }

      @Override
      public StateMachineInterceptor<OrderState, String> getInterceptor() {
        return new StateMachineInterceptorAdapter<>();
      }
    };
  }
}
