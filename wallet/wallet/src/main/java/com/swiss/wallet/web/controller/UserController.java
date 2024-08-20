package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.UserService;
import com.swiss.wallet.web.dto.UserAddressCreateDto;
import com.swiss.wallet.web.dto.UserPasswordRecoveryDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users", description = "Contains all operations related to resources for registering, editing and reading a user.")
@RestController
@RequestMapping("/api/v3/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Create a new user", description = "Feature to create a new user",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Resource created successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "409", description = "Email user already registered in the system",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "422", description = "Resource not processed due to invalid input data",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody @Valid UserAddressCreateDto userAddressCreateDto){
        UserEntity user = userService.saveUser(userAddressCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.toUserResponse(user));
    }

    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestParam String username){
        String code = userService.recoverPassword(username);
        return ResponseEntity.ok().body(code);
    }

    //Method for changing a forgotten user password
    @PutMapping("/recover-password")
    public ResponseEntity<Void> updateForgottenPassword(@RequestBody UserPasswordRecoveryDto passwordRecoveryDto){
        userService.changeForgottenPassword(passwordRecoveryDto);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/current")
    @PreAuthorize("hasRole('CLIENT')")
    public ResponseEntity<UserResponseDto> getCurrentUser(@AuthenticationPrincipal JwtUserDetails userDetails){
        UserEntity user = userService.findById(userDetails.getId());
        return ResponseEntity.ok().body(UserResponseDto.toUserResponse(user));
    }

}
