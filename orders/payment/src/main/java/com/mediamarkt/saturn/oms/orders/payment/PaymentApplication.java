package com.mediamarkt.saturn.oms.orders.payment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.mediamarkt.saturn.oms.orders.payment",
    "com.mediamarkt.saturn.oms.statemachine",
    "com.mediamarkt.saturn.oms.common.exception"
})
public class PaymentApplication {

  public static void main(String[] args) {
    SpringApplication.run(PaymentApplication.class, args);
  }
}
