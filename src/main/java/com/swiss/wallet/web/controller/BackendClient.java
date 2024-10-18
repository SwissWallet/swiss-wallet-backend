package com.swiss.wallet.web.controller;

import com.swiss.wallet.web.dto.BankUserCreateDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "backendBank", url = "http://localhost:8000/api/v1")
public interface BackendClient {

    @PostMapping("/users")
    String saveUser(@RequestBody BankUserCreateDto bankUserCreateDto);

    @
}
