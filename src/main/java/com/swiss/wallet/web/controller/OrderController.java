package com.swiss.wallet.web.controller;


import com.swiss.wallet.entity.Order;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.OrderService;
import com.swiss.wallet.web.dto.OrderResponseDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Save a new order", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource saved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderResponseDto> saveOrder(@AuthenticationPrincipal JwtUserDetails userDetails,
                                                      @RequestParam("idProduct") Long idProduct){
        Order order = orderService.saveOrder(userDetails.getId(), idProduct);
        return ResponseEntity.ok().body(OrderResponseDto.toOrderResponse(order));
    }

    @Operation(summary = "Recover order logged in user", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "204", description = "Resource successfully retrieved empty list",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/current")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<List<OrderResponseDto>> getAllByUser(@AuthenticationPrincipal JwtUserDetails userDetails){
        List<Order> orders = orderService.findAllByUser(userDetails.getId());
        if (orders.isEmpty()){
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(OrderResponseDto.toListOrderResponse(orders));
    }


    @Operation(summary = "Delete order by id", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @DeleteMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> removeOrder(@AuthenticationPrincipal JwtUserDetails userDetails,
                                            @RequestParam("idOrder") Long idOrder){
        orderService.deleteByIdAndUser(idOrder, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Recover all order", description = "Request requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "204", description = "Resource successfully retrieved empty list",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderResponseDto>> getAllOrder(){
        List<Order> orders = orderService.findAll();
        if (orders.isEmpty()){
            return ResponseEntity.noContent().build();
        }
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
