package com.mediamarkt.saturn.oms.orders.create.application.usecase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import com.mediamarkt.saturn.oms.statemachine.service.StateMachineService;
import com.medimarkt.saturn.oms.domain.model.Basket;
import com.medimarkt.saturn.oms.domain.model.BasketItem;
import com.medimarkt.saturn.oms.domain.model.Item;
import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import com.medimarkt.saturn.oms.domain.repository.BasketItemRepository;
import com.medimarkt.saturn.oms.domain.repository.ItemRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderWriteRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class CreateOrderServiceTest {

  @InjectMocks
  private CreateOrderService createOrderService;

  @Mock
  private OrderWriteRepository orderWriteRepository;

  @Mock
  private ItemRepository itemRepository;

  @Mock
  private BasketItemRepository basketItemRepository;

  @Mock
  private StateMachineService stateMachineService;

  private Basket testBasket;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);

    Item testItem = Item.builder().id(1L).name("Test Item").price(BigDecimal.valueOf(100.0)).stock(10).build();

    BasketItem basketItem = BasketItem.builder().item(testItem).quantity(2).build();

    testBasket = Basket.builder().items(List.of(basketItem)).build();
  }

  @Test
  void testCreateOrderSuccess() {
    when(itemRepository.findStockByIds(anyList())).thenReturn(Map.of(1L, 10));

    when(orderWriteRepository.save(any(Order.class))).thenAnswer(invocation -> {
      Order order = invocation.getArgument(0);
      return order.toBuilder().id(1L).build();
    });

    Order result = createOrderService.execute(testBasket);

    verify(basketItemRepository, times(1)).saveBasketItem(eq(1L), eq(1L), eq(2));
    verify(itemRepository, times(1)).decreaseStock(eq(1L), eq(2));
    verify(stateMachineService, times(1)).saveInitialState(eq("1"), eq(OrderState.CREATED));

    assertEquals(1L, result.getId());
    assertEquals(OrderState.CREATED, result.getState());
    assertEquals(1, result.getItems().size());
  }

  @Test
  void testCreateOrderFailsDueToInsufficientStock() {
    when(itemRepository.findStockByIds(anyList())).thenReturn(Map.of(1L, 1));

    assertThrows(IllegalStateException.class, () -> createOrderService.execute(testBasket));

    verifyNoInteractions(orderWriteRepository);
    verifyNoInteractions(basketItemRepository);
    verifyNoInteractions(stateMachineService);
  }

  @Test
  void testCreateOrderFailsDueToStateMachineError() {
    when(itemRepository.findStockByIds(anyList())).thenReturn(Map.of(1L, 10));

    when(orderWriteRepository.save(any(Order.class))).thenAnswer(invocation -> {
      Order order = invocation.getArgument(0);
      return order.toBuilder().id(1L).build();
    });

    doThrow(new RuntimeException("Error saving initial state")).when(stateMachineService).saveInitialState(eq("1"), eq(OrderState.CREATED));

    assertThrows(RuntimeException.class, () -> createOrderService.execute(testBasket));

    verify(basketItemRepository, times(1)).saveBasketItem(eq(1L), eq(1L), eq(2));
    verify(itemRepository, times(1)).decreaseStock(eq(1L), eq(2));
  }
}
