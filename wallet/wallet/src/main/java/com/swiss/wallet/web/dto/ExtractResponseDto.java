package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Extract;

public record ExtractResponseDto(Double value,
                                 Extract.Type type,
                                 String description,
                                 AccountResponseDto account
                                 ) {
    public ExtractResponseDto(Double value, Extract.Type type, String description, AccountResponseDto account) {
        this.value = value;
        this.type = type;
        this.description = description;
        this.account = account;
    }

    public static ExtractResponseDto toExtractResponse(Extract extract){
        return new ExtractResponseDto(
                extract.getValue(),
                extract.getType(),
                extract.getDescription(),
                AccountResponseDto.toAccountResponseDto(extract.getAccount())
        );
    }

}
