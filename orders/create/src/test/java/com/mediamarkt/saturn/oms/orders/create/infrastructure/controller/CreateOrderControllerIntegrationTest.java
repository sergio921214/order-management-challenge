package com.mediamarkt.saturn.oms.orders.create.infrastructure.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateOrderControllerIntegrationTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private JdbcTemplate jdbcTemplate;

  @Test
  public void testCreateOrder_Success() throws Exception {
    String requestBody = """
            {
              "items": [
                {"itemId": 1, "quantity": 2},
                {"itemId": 2, "quantity": 3}
              ]
            }
        """;

    mockMvc.perform(post("/orders").contentType("application/json").content(requestBody)).andExpect(status().isOk())
        .andExpect(jsonPath("$.state").value("CREATED")).andExpect(jsonPath("$.id").isNotEmpty());

    Integer orderCount = jdbcTemplate.queryForObject("SELECT COUNT(*) FROM orders WHERE state = 'CREATED'", Integer.class);
    assert orderCount == 1;

    Integer stockItem1 = jdbcTemplate.queryForObject("SELECT stock FROM items WHERE id = 1", Integer.class);
    Integer stockItem2 = jdbcTemplate.queryForObject("SELECT stock FROM items WHERE id = 2", Integer.class);

    assert stockItem1 == 98;
    assert stockItem2 == 47;
  }
}

