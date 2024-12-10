package com.mediamarkt.saturn.oms.statemachine.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity

@Table(schema = "order_management", name = "state_machine_context")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StateMachineContextEntity {

  @Id
  @Column(name = "machine_id")
  private String machineId;

  @Column(name = "state", nullable = false)
  private String state;

  @Column(name = "last_updated")
  private LocalDateTime lastUpdated;

}

