package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.UserService;
import com.swiss.wallet.web.dto.*;
import com.swiss.wallet.web.exception.ErrorMessage;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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

    @Operation(summary = "Recover a logged in user", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @GetMapping("/current")
    @PreAuthorize("hasRole('CLIENT') or hasRole('ADMIN')")
    public ResponseEntity<ResponseGlobalDto> getCurrentUser(@AuthenticationPrincipal JwtUserDetails userDetails){
        ResponseGlobalDto responseGlobalDto = userService.findByIdGlobal(userDetails.getId());
        return ResponseEntity.ok().body(responseGlobalDto);
    }

    @Operation(summary = "Generate code to change password", description = "Resource to generate code",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class)))
            })
    @PostMapping("/recover-password")
    public ResponseEntity<String> recoverPassword(@RequestParam String username){
        String code = userService.recoverPassword(username);
        return ResponseEntity.ok().body(code);
    }

    @Operation(summary = "Change forgotten password", description = "Resource to change forgotten password",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "404", description = "Resource not found",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "400", description = "Verification code does not match",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),

            })
    @PutMapping("/recover-password")
    public ResponseEntity<Void> updateForgottenPassword(@RequestBody @Valid UserPasswordRecoveryDto passwordRecoveryDto){
        userService.changeForgottenPassword(passwordRecoveryDto);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Change user password", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "400", description = "Password provided invalid or new password provided is invalid",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PutMapping("/password")
    @PreAuthorize("hasRole('CLIENT') OR hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserPassword(@RequestBody @Valid UserPasswordChangeDto passwordChangeDto,
                                                   @AuthenticationPrincipal JwtUserDetails userDetails){
        userService.changeUserPassword(passwordChangeDto, userDetails.getId());
        return ResponseEntity.ok().build();
    }


    @Operation(summary = "Change user address", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                   @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @PutMapping("/address")
    @PreAuthorize("hasRole('CLIENT') OR hasRole('ADMIN')")
    public ResponseEntity<Void> updateUserAddress(@RequestBody @Valid AddressCreateDto addressCreateDto,
                                                   @AuthenticationPrincipal JwtUserDetails userDetails){
        userService.changeUserAddress(addressCreateDto, userDetails.getId());
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete logged in user account", description = "Request requires a Bearer Token. Restricted access to CLIENT",
            security = @SecurityRequirement(name = "security"),
            responses = {
                    @ApiResponse(responseCode = "200", description = "Resource retrieved successfully",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDto.class))),
                    @ApiResponse(responseCode = "403", description = "User not allowed to access this resource",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorMessage.class))),
            })
    @DeleteMapping
    @PreAuthorize("hasRole('CLIENT') OR hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@AuthenticationPrincipal JwtUserDetails userDetails){

        userService.deleteUser(userDetails.getId());
        return ResponseEntity.ok().build();
    }

}
