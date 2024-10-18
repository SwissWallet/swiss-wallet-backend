package com.swiss.wallet.web.controller;

import com.swiss.wallet.web.dto.BankUserCreateDto;
import com.swiss.wallet.web.dto.UserPasswordChangeDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "backendBank", url = "http://localhost:8000/api/v1")
public interface BackendClient {

    @PostMapping("/users")
    String saveUser(@RequestBody BankUserCreateDto bankUserCreateDto);

    @PutMapping("/users/password")
    String updatePassword(@RequestBody UserPasswordChangeDto passwordChangeDto, @RequestParam("id") Long id);
}
