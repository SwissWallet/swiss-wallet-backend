package com.swiss.wallet.web.dto;

import jakarta.validation.Valid;

public record UserAddressCreateDto (@Valid
                                    UserCreateDto user,
                                    @Valid
                                    AddressCreateDto address) {
}
