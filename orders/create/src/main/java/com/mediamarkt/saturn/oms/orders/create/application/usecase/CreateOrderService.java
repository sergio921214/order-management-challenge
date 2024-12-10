package com.mediamarkt.saturn.oms.orders.create.application.usecase;

import static com.medimarkt.saturn.oms.domain.service.BasketValidator.validateBasketStock;
import static com.medimarkt.saturn.oms.domain.service.BasketValidator.validateNonEmptyBasket;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.mediamarkt.saturn.oms.statemachine.service.StateMachineService;
import com.medimarkt.saturn.oms.domain.model.Basket;
import com.medimarkt.saturn.oms.domain.model.Order;
import com.medimarkt.saturn.oms.domain.model.enums.OrderState;
import com.medimarkt.saturn.oms.domain.repository.BasketItemRepository;
import com.medimarkt.saturn.oms.domain.repository.ItemRepository;
import com.medimarkt.saturn.oms.domain.repository.OrderWriteRepository;
import com.medimarkt.saturn.oms.domain.usecase.CreateOrderUseCase;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@AllArgsConstructor
public class CreateOrderService implements CreateOrderUseCase {

  private final OrderWriteRepository orderWriteRepository;

  private final ItemRepository itemRepository;

  private final BasketItemRepository basketItemRepository;

  private final StateMachineService stateMachineService;

  @Override
  @Transactional
  public Order execute(Basket basket) {
    validateNonEmptyBasket(basket);
    Map<Long, Integer> stockMap = this.getStockForItems(basket);
    validateBasketStock(basket, stockMap);

    Order order = Order.builder().items(basket.getItems()).state(OrderState.CREATED).build();
    Order savedOrder = orderWriteRepository.save(order);

    basket.getItems().forEach(
        basketItem -> basketItemRepository.saveBasketItem(savedOrder.getId(), basketItem.getItem().getId(), basketItem.getQuantity()));

    basket.getItems().forEach(basketItem -> itemRepository.decreaseStock(basketItem.getItem().getId(), basketItem.getQuantity()));

    stateMachineService.saveInitialState(String.valueOf(savedOrder.getId()), OrderState.CREATED);

    return savedOrder;
  }

  private Map<Long, Integer> getStockForItems(Basket basket) {
    List<Long> itemIds = basket.getItems().stream().map(item -> item.getItem().getId()).collect(Collectors.toList());
    return itemRepository.findStockByIds(itemIds);
  }

}
