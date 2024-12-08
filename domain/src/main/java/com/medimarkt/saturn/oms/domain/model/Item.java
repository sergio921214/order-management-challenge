package com.medimarkt.saturn.oms.domain.model;

import java.math.BigDecimal;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class Item {

  private Long id;

  private String name;

  private BigDecimal price;

  private int stock;
}
