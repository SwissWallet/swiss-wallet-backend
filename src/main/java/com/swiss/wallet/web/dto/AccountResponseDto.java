package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Account;

import java.util.List;
import java.util.stream.Collectors;

public record AccountResponseDto(Long id,
                                 Double value) {

    public static AccountResponseDto toAccountResponseDto(Account account){
        return new AccountResponseDto(
                account.getId(),
                account.getValue()
        );
    }
    public static List<AccountResponseDto> toListAccountResponse(List<Account> accounts){
        return accounts.stream()
                .map(account -> toAccountResponseDto(account)).collect(Collectors.toList());
    }
}
