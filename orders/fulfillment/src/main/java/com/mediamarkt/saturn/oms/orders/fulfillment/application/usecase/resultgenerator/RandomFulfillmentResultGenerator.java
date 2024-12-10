package com.mediamarkt.saturn.oms.orders.fulfillment.application.usecase.resultgenerator;

import java.util.Random;

import org.springframework.stereotype.Component;

@Component
public class RandomFulfillmentResultGenerator implements FulfillmentResultGenerator {

  private final Random random = new Random();

  @Override
  public String generate() {
    return random.nextBoolean() ? "success" : "failure";
  }
}

