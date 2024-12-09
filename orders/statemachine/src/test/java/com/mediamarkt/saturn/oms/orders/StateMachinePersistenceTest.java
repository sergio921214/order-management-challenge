package com.mediamarkt.saturn.oms.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDateTime;

import com.mediamarkt.saturn.oms.statemachine.StateMachineApplication;
import com.mediamarkt.saturn.oms.statemachine.entity.StateMachineContextEntity;
import com.mediamarkt.saturn.oms.statemachine.repository.StateMachineContextRepository;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.StateMachineContext;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;
import org.springframework.statemachine.support.DefaultStateMachineContext;

@SpringBootTest(classes = StateMachineApplication.class)
public class StateMachinePersistenceTest {

  @Autowired
  private StateMachineRuntimePersister<OrderState, String, String> persister;

  @Autowired
  private StateMachineContextRepository repository;

  @AfterEach
  public void cleanUp() {
    repository.deleteAll();
  }

  @SneakyThrows
  @Test
  public void testPersistState() {
    StateMachineContext<OrderState, String> context = new DefaultStateMachineContext<>(OrderState.PAID, null, null, null);
    persister.write(context, "testMachineId");

    StateMachineContextEntity entity = repository.findById("testMachineId").orElse(null);
    assertNotNull(entity);
    assertEquals(OrderState.PAID.name(), entity.getState());
  }

  @SneakyThrows
  @Test
  public void testRetrieveState() {
    StateMachineContextEntity entity = StateMachineContextEntity.builder()
        .machineId("testMachineId")
        .state(OrderState.PAID.name())
        .lastUpdated(LocalDateTime.now()).build();
    repository.save(entity);

    StateMachineContext<OrderState, String> context = persister.read("testMachineId");

    assertNotNull(context);
    assertEquals(OrderState.PAID, context.getState());
  }
}

