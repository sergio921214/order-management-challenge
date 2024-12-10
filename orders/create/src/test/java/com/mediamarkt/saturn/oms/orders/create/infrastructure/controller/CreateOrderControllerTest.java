package com.mediamarkt.saturn.oms.orders.create.infrastructure.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mediamarkt.saturn.oms.orders.create.CreateOrderApplication;
import com.mediamarkt.saturn.oms.orders.create.infrastructure.mapper.CreateOrderRequestMapper;
import com.mediamarkt.saturn.oms.orders.create.infrastructure.mapper.CreateOrderResponseMapper;
import com.medimarkt.saturn.oms.domain.model.Basket;
import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import com.medimarkt.saturn.oms.domain.usecase.CreateOrderUseCase;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.openapitools.model.BasketDTO;
import org.openapitools.model.OrderDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = CreateOrderController.class)
@ContextConfiguration(classes = CreateOrderApplication.class)
class CreateOrderControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @MockBean
  private CreateOrderUseCase createOrderUseCase;

  @MockBean
  private CreateOrderRequestMapper requestMapper;

  @MockBean
  private CreateOrderResponseMapper responseMapper;

  @Test
  void createOrder_shouldReturnOk() throws Exception {
    BasketDTO basketDTO = new BasketDTO().items(Collections.emptyList());
    Basket basket = Basket.builder().items(Collections.emptyList()).build();
    Order order = Order.builder().id(1L).state(OrderState.CREATED).build();
    OrderDTO orderDTO = new OrderDTO().id(1).state(OrderDTO.StateEnum.CREATED);

    Mockito.when(requestMapper.mapToBasket(any())).thenReturn(basket);
    Mockito.when(createOrderUseCase.execute(basket)).thenReturn(order);
    Mockito.when(responseMapper.mapToOrderDTO(order)).thenReturn(orderDTO);

    mockMvc.perform(post("/orders")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(basketDTO)))
        .andExpect(status().isOk());
  }
}
