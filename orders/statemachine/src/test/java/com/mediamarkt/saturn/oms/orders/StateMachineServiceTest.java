package com.mediamarkt.saturn.oms.orders;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.mediamarkt.saturn.oms.statemachine.StateMachineApplication;
import com.mediamarkt.saturn.oms.statemachine.entity.StateMachineContextEntity;
import com.mediamarkt.saturn.oms.statemachine.repository.StateMachineContextRepository;
import com.mediamarkt.saturn.oms.statemachine.service.StateMachineService;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import lombok.SneakyThrows;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.statemachine.persist.StateMachineRuntimePersister;

@SpringBootTest(classes = StateMachineApplication.class)
public class StateMachineServiceTest {

  @Autowired
  private StateMachineService stateMachineService;

  @Autowired
  private StateMachineContextRepository repository;

  @Autowired
  private StateMachineRuntimePersister<OrderState, String, String> persister;

  @AfterEach
  public void cleanUp() {
    repository.deleteAll();
  }

  @SneakyThrows
  @Test
  public void testSendEvent() {

    stateMachineService.sendEvent("testMachineId", "PAY");
    StateMachineContextEntity entity = repository.findById("testMachineId").orElse(null);

    assertNotNull(entity);
    assertEquals(OrderState.PAID.name(), entity.getState());
  }
}
