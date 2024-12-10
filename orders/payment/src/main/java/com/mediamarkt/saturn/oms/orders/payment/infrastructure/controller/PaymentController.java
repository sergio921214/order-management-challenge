package com.mediamarkt.saturn.oms.orders.payment.infrastructure.controller;

import com.mediamarkt.saturn.oms.orders.payment.application.usecase.ConfirmPaymentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class PaymentController {

  private final ConfirmPaymentService confirmPaymentService;

  @PostMapping("/{id}/pay")
  public ResponseEntity<Void> confirmPayment(@PathVariable("id") Long orderId) {
    confirmPaymentService.execute(orderId);
    return ResponseEntity.ok().build();
  }
}
