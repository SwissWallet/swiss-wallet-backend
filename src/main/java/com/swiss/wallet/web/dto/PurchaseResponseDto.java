package com.swiss.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swiss.wallet.web.dto.UserResponseDto;

import java.time.LocalDate;

public record PurchaseResponseDto(Long id,
                                  UserResponseDto user,
                                  float value,
                                  @JsonFormat(pattern = "dd/MM/yyyy")
                                  LocalDate datePurchase,  // Mudança para LocalDate
                                  String status) {

}
