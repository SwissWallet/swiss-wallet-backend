package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Address;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record AddressCreateDto (@NotBlank
                                String zipCode,
                                @NotBlank
                                String street,
                                @NotBlank
                                String city,
                                @NotNull
                                Long number,
                                @NotBlank
                                String uf) {

    public Address toAddress(){
        return new Address(
                zipCode,
                street,
                city,
                number,
                uf
        );
    }
}
