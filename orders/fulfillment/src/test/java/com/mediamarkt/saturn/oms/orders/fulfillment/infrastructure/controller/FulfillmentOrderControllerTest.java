package com.mediamarkt.saturn.oms.orders.fulfillment.infrastructure.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mediamarkt.saturn.oms.orders.fulfillment.FulfillmentOrderApplication;
import com.medimarkt.saturn.oms.domain.usecase.FulfillmentOrderUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = FulfillmentOrderController.class)
@ContextConfiguration(classes = FulfillmentOrderApplication.class)
public class FulfillmentOrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private FulfillmentOrderUseCase fulfillmentOrderUseCase;

  @Test
  void fulfillOrder_ShouldReturnOk() throws Exception {
    Long orderId = 1L;

    Mockito.doNothing().when(fulfillmentOrderUseCase).execute(orderId);

    mockMvc.perform(post("/orders/{orderId}/fulfill", orderId)).andExpect(status().isOk());
  }

  @Test
  void fulfillOrder_OrderNotFound_ShouldReturn409() throws Exception {
    Long orderId = 99L;

    Mockito.doThrow(new IllegalStateException("Order not found")).when(fulfillmentOrderUseCase).execute(orderId);

    mockMvc.perform(post("/orders/{orderId}/fulfill", orderId)).andExpect(status().isConflict())
        .andExpect(jsonPath("$.message").value("Order not found"));
  }

}
