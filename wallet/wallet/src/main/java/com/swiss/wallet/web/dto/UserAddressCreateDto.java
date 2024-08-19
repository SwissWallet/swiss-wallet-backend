package com.swiss.wallet.web.dto;

public record UserAddressCreateDto (UserCreateDto user,
                                    AddressCreateDto address) {
}
