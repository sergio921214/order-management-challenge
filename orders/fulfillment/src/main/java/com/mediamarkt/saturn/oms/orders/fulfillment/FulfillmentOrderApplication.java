package com.mediamarkt.saturn.oms.orders.fulfillment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = {
    "com.mediamarkt.saturn.oms.orders.fulfillment",
    "com.mediamarkt.saturn.oms.common.exception",
    "com.mediamarkt.saturn.oms.statemachine"
})
public class FulfillmentOrderApplication {

  public static void main(String[] args) {
    SpringApplication.run(FulfillmentOrderApplication.class, args);
  }
}
