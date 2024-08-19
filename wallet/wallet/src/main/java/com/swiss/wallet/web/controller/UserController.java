package com.swiss.wallet.web.controller;

import com.swiss.wallet.entity.UserEntity;
import com.swiss.wallet.service.UserService;
import com.swiss.wallet.web.dto.UserAddressCreateDto;
import com.swiss.wallet.web.dto.UserResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v3/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserResponseDto> saveUser(@RequestBody UserAddressCreateDto userAddressCreateDto){
        UserEntity user = userService.saveUser(userAddressCreateDto);
        return ResponseEntity.ok().body(UserResponseDto.toUserResponse(user));
    }
}
