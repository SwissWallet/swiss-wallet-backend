package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Address;

public record AddressResponseDto (String street,
                                  String city,
                                  Long number) {

    public AddressResponseDto(String street, String city, Long number) {
        this.street = street;
        this.city = city;
        this.number = number;
    }

    public static AddressResponseDto toAddressResponse(Address address){
        return new AddressResponseDto(
                address.getStreet(),
                address.getCity(),
                address.getNumber()
        );
    }
}
