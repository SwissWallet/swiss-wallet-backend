package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public record UserResponseDto (String username,
                               String name,
                               String birthDate,
                               String phone,
                               AddressResponseDto address) {

    public UserResponseDto(String username, String name, String birthDate, String phone, AddressResponseDto address) {
        this.username = username;
        this.name = name;
        this.birthDate = birthDate;
        this.phone = phone;
        this.address = address;
    }

    public static UserResponseDto toUserResponse(UserEntity user){
        return new UserResponseDto(
                user.getUsername(),
                user.getName(),
                user.getBirthDate(),
                user.getPhone(),
                AddressResponseDto.toAddressResponse(user.getAddress())
        );
    }

    public static List<UserResponseDto> toListUserResponse(List<UserEntity> users){
        return users.stream()
                .map(user -> toUserResponse(user)).collect(Collectors.toList());
    }

}
