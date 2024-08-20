package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.jwt.JwtUserDetails;
import com.swiss.wallet.service.UserService;
import com.swiss.wallet.web.dto.UserAddressCreateDto;
import com.swiss.wallet.web.dto.UserPasswordRecoveryDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v3/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody @Valid UserAddressCreateDto userAddressCreateDto){
        UserEntity user = userService.saveUser(userAddressCreateDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserResponseDto.toUserResponse(user));
    }

    //Method to generate forgotten code the password
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
