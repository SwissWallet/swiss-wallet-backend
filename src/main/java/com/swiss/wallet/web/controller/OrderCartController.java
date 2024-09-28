package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.OrderCart;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.OrderCartService;
import com.swiss.wallet.web.dto.OrderCartCreateDto;
import com.swiss.wallet.web.dto.OrderCartResponseDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Order Cart", description = "Contains all operations related to resources for registering, editing and reading a purchase.")
@RestController
@RequestMapping("/api/v3/order/carts")
public class OrderCartController {

    private final OrderCartService orderCartService;

    public OrderCartController(OrderCartService orderCartService) {
        this.orderCartService = orderCartService;
    }

    @Operation(summary = "Create a new order cart", description = "Request requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Insufficient ponint balance",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                     @ApiResponse(responseCode = "404", description = "User or Products not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<OrderCartResponseDto> createOrderCart(@RequestBody @Valid OrderCartCreateDto orderCartCreateDto, @AuthenticationPrincipal JwtUserDetails userDetails) {
        OrderCart orderCart = orderCartService.saveProductsInOrderCart(userDetails.getId(), orderCartCreateDto);
        return ResponseEntity.ok().body(OrderCartResponseDto.toPurchaseResponse(orderCart));
    }

    @Operation(summary = "Recover all order cart", description = "Request requires a Bearer Token. Restricted access to ADMIN",
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
    public ResponseEntity<List<OrderCartResponseDto>> listAllOrderCarts(){
        List<OrderCart> orderCarts = orderCartService.findAll();
        if(!orderCarts.isEmpty()){
            return ResponseEntity.ok().body(OrderCartResponseDto.toListProductResponse(orderCarts));
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Recover order cart user", description = "Request requires a Bearer Token. Restricted access to ADMIN",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "204", description = "Resource successfully retrieved empty list",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<OrderCartResponseDto>> listAllByUser(@RequestParam String username){
        List<OrderCart> orderCarts = orderCartService.findAllByUser(username);
        if(!orderCarts.isEmpty()){
            return ResponseEntity.ok().body(OrderCartResponseDto.toListProductResponse(orderCarts));
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Recover order cart logged in user", description = "Request requires a Bearer Token. Restricted access to CLIENT",
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
    public ResponseEntity<List<OrderCartResponseDto>> listAllByLoggedUser(@AuthenticationPrincipal JwtUserDetails userDetails){
        List<OrderCart> orderCarts = orderCartService.findAllByUser(userDetails.getUsername());
        if(!orderCarts.isEmpty()){
            return ResponseEntity.ok().body(OrderCartResponseDto.toListProductResponse(orderCarts));
        }
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Pay order cart of user", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Resource not processed, order cart already paid",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PostMapping("/paid/{idOrderCart}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> paymentOrderCart(
            @AuthenticationPrincipal JwtUserDetails userDetails,
            @PathVariable("idOrderCart") Long idOrderCart
    ){
        orderCartService.paymentOrderCart(userDetails.getId(), idOrderCart);
        return ResponseEntity.accepted().build();
    }

    @Operation(summary = "Delete order cart", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "204", description = "Resource successfully retrieved empty list",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @DeleteMapping("/{idOrderCart}")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<Void> removeOrderCart(@PathVariable Long idOrderCart){
        orderCartService.cancelOrderCart(idOrderCart);
        return ResponseEntity.ok().build();
    }
}
