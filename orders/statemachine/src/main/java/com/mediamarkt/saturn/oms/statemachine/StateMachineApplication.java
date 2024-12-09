package com.mediamarkt.saturn.oms.statemachine;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.mediamarkt.saturn.oms.statemachine.entity")
@EnableJpaRepositories(basePackages = "com.mediamarkt.saturn.oms.statemachine.repository")
public class StateMachineApplication {

  public static void main(String[] args) {
    SpringApplication.run(StateMachineApplication.class, args);
  }
}
