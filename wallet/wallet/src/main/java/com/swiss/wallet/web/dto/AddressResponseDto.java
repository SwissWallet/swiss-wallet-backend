package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Address;

public record AddressResponseDto (Long id,
                                  String zipCode,
                                  String street,
                                  String city,
                                  Long number,
                                  String uf
                                ) {

    public AddressResponseDto(Long id, String zipCode, String street, String city, Long number, String uf) {
        this.id = id;
        this.zipCode = zipCode;
        this.street = street;
        this.city = city;
        this.number = number;
        this.uf = uf;
    }

    public static AddressResponseDto toAddressResponse(Address address){
        return new AddressResponseDto(
                address.getId(),
                address.getZipCode(),
                address.getStreet(),
                address.getCity(),
                address.getNumber(),
                address.getUf()
        );
    }
}
