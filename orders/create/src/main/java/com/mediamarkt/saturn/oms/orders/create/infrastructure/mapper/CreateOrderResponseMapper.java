package com.mediamarkt.saturn.oms.orders.create.infrastructure.mapper;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

import com.medimarkt.saturn.oms.domain.model.BasketItem;
import com.medimarkt.saturn.oms.domain.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.openapitools.model.ItemDTO;
import org.openapitools.model.OrderDTO;

@Mapper(componentModel = "spring")
public interface CreateOrderResponseMapper {

  @Mapping(target = "id", source = "id")
  @Mapping(target = "state", source = "state")
  @Mapping(target = "createdAt", source = "createdAt", qualifiedByName = "localDateTimeToOffsetDateTime")
  @Mapping(target = "updatedAt", source = "updatedAt", qualifiedByName = "localDateTimeToOffsetDateTime")
  @Mapping(target = "items", source = "items")
  OrderDTO mapToOrderDTO(Order order);

  @Named("localDateTimeToOffsetDateTime")
  default OffsetDateTime localDateTimeToOffsetDateTime(LocalDateTime localDateTime) {
    return localDateTime != null ? localDateTime.atOffset(ZoneOffset.UTC) : null;
  }

  @Mapping(target = "id", source = "item.id")
  ItemDTO mapToItemDTO(BasketItem basketItem);
}
