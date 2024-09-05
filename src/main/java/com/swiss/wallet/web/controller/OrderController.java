package com.swiss.wallet.web.controller;


import com.swiss.wallet.entity.Order;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.OrderService;
import com.swiss.wallet.web.dto.OrderResponseDto;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order", description = "Contains all operations related to resources for registering, editing and reading a order.")
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

    @DeleteMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> removeOrder(@AuthenticationPrincipal JwtUserDetails userDetails,
                                            @RequestParam("idOrder") Long idOrder){
        orderService.deleteByIdAndUser(idOrder, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrder(){
        List<Order> orders = orderService.findAll();
        return ResponseEntity.ok().body(OrderResponseDto.toListOrderResponse(orders));
    }

    @GetMapping("/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> listOrderByFilter(@RequestParam("status") String status){
        List<Order> orders = orderService.findAllByStatus(status);
        return ResponseEntity.ok(OrderResponseDto.toListOrderResponse(orders));
    }

    @PutMapping("/change-status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDto> changeStatus(@RequestParam("idOrder") Long idOrder,
                                                         @RequestParam("status") String status){
        Order order = orderService.changeStatus(idOrder, status);
        return ResponseEntity.ok(OrderResponseDto.toOrderResponse(order));
    }
}
