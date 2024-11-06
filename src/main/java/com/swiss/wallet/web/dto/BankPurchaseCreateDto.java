package com.swiss.wallet.web.dto;


public record BankPurchaseCreateDto(String username,
                                    String typePayment,
                                    Double value) {
}
