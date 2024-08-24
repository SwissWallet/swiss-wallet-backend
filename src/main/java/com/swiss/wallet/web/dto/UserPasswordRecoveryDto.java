package com.swiss.wallet.web.dto;

public record UserPasswordRecoveryDto(String username,
                                      String newPassword,
                                      String verificationCode) {
}
