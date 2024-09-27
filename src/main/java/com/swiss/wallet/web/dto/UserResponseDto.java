package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Role;
import com.swiss.wallet.entity.UserEntity;

import java.util.List;
import java.util.stream.Collectors;

public record UserResponseDto (Long id,
                               String username,
                               String name,
                               String cpf,
                               String birthDate,
                               String phone,
                               Role role
                               ) {

    public static UserResponseDto toUserResponse(UserEntity user){
        return new UserResponseDto(
                user.getId(),
                user.getUsername(),
                user.getName(),
                user.getCpf(),
                user.getBirthDate(),
                user.getPhone(),
                user.getRole()
        );
    }

    public static List<UserResponseDto> toListUserResponse(List<UserEntity> users){
        return users.stream()
                .map(user -> toUserResponse(user)).collect(Collectors.toList());
    }
}
