package com.swiss.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swiss.wallet.entity.OrderCart;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record OrderCartResponseDto(Long id,
                                   UserResponseDto user,
                                   List<ProductResponseDto> product,
                                   float value,
                                   String status,
                                   @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
                                   LocalDateTime dateTime) {


    public static OrderCartResponseDto toPurchaseResponse(OrderCart orderCart){
        return new OrderCartResponseDto(
                orderCart.getId(),
                UserResponseDto.toUserResponse(orderCart.getUser()),
                ProductResponseDto.toListProductResponse(orderCart.getProducts()),
                orderCart.getValue(),
                orderCart.getStatus().name(),
                orderCart.getDateTime()
        );
    }

    public static List<OrderCartResponseDto> toListProductResponse(List<OrderCart> orderCarts){
        return orderCarts.stream()
                .map(orderCart -> toPurchaseResponse(orderCart)).collect(Collectors.toList());
    }
}
