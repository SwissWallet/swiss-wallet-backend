package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Address;

public record AddressCreateDto (String zipCode,
                                String street,
                                String city,
                                Long number,
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
