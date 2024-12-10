package com.mediamarkt.saturn.oms.orders.create;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.mediamarkt.saturn.oms.orders.create",
    "com.mediamarkt.saturn.oms.statemachine",
    "com.mediamarkt.saturn.oms.common.exception"
})
public class CreateOrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(CreateOrderApplication.class, args);
  }
}
