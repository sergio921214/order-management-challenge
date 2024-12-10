package com.mediamarkt.saturn.oms.orders.payment.infrastructure.controller;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.mediamarkt.saturn.oms.orders.payment.PaymentApplication;
import com.mediamarkt.saturn.oms.orders.payment.application.usecase.ConfirmPaymentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PaymentController.class)
@ContextConfiguration(classes = PaymentApplication.class)
class PaymentControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ConfirmPaymentService confirmPaymentService;

  @Test
  void testConfirmPayment() throws Exception {
    Long orderId = 1L;

    doNothing().when(confirmPaymentService).execute(orderId);

    mockMvc.perform(post("/orders/{id}/pay", orderId).contentType(MediaType.APPLICATION_JSON)).andExpect(status().isOk());

    verify(confirmPaymentService, Mockito.times(1)).execute(orderId);
  }
}
