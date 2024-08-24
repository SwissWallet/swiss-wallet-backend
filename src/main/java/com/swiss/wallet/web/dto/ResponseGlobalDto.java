package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Account;
import com.swiss.wallet.entity.Address;
import com.swiss.wallet.entity.Extract;
import com.swiss.wallet.entity.UserEntity;

import java.util.List;

public record ResponseGlobalDto (UserResponseDto user,
                                 AddressResponseDto address,
                                 AccountResponseDto account,
                                 List<ExtractResponseDto> extracts){

    public ResponseGlobalDto(UserResponseDto user, AddressResponseDto address, AccountResponseDto account, List<ExtractResponseDto> extracts) {
        this.user = user;
        this.address = address;
        this.account = account;
        this.extracts = extracts;
    }

    public static ResponseGlobalDto toResponseGlobal(UserEntity user,
                                                     Address address,
                                                     Account account,
                                                     List<Extract> extracts){
        return new ResponseGlobalDto(
                UserResponseDto.toUserResponse(user),
                AddressResponseDto.toAddressResponse(address),
                AccountResponseDto.toAccountResponseDto(account),
                ExtractResponseDto.toListExtractResponse(extracts)
        );
    }
}
