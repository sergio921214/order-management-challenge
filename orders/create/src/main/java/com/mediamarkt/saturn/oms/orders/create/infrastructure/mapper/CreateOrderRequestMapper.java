package com.mediamarkt.saturn.oms.orders.create.infrastructure.mapper;

import com.medimarkt.saturn.oms.domain.model.Basket;
import com.medimarkt.saturn.oms.domain.model.BasketItem;
import com.medimarkt.saturn.oms.domain.model.Item;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.openapitools.model.BasketDTO;
import org.openapitools.model.BasketItemsInnerDTO;

@Mapper(componentModel = "spring")
public interface CreateOrderRequestMapper {

  Basket mapToBasket(BasketDTO basketDTO);

  @Mapping(target = "item.id", source = "itemId")
  BasketItem mapToBasketItem(BasketItemsInnerDTO basketItemsInnerDTO);

  default Item mapToItem(Integer itemId) {
    return Item.builder()
        .id(itemId != null ? Long.valueOf(itemId) : null)
        .build();
  }
}
