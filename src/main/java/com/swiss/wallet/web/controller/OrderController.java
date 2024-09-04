package com.swiss.wallet.web.controller;


import com.swiss.wallet.entity.Order;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.OrderService;
import com.swiss.wallet.web.dto.OrderResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v3/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderResponseDto> saveOrder(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                      @RequestParam("idProduct") Long idProduct){
        Order order = orderService.saveOrder(userDetails.getId(), idProduct);
        return ResponseEntity.ok().body(OrderResponseDto.toOrderResponse(order));
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<OrderResponseDto>> getAllByUser(@AuthenticationPrincipal JwtUserDetails userDetails){
        List<Order> orders = orderService.findAllByUser(userDetails.getId());
        return ResponseEntity.ok().body(OrderResponseDto.toListOrderResponse(orders));
    }

}
