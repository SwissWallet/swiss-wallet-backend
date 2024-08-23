package com.swiss.wallet.web.dto;

import com.swiss.wallet.entity.Extract;

import java.time.LocalDateTime;

public record ExtractResponseDto(Double value,
                                 Extract.Type type,
                                 String description,
                                 AccountResponseDto account,
                                 LocalDateTime date
                                 ) {
    public ExtractResponseDto(Double value, Extract.Type type, String description, AccountResponseDto account, LocalDateTime date) {
        this.value = value;
        this.type = type;
        this.description = description;
        this.account = account;
        this.date = date;
    }

    public static ExtractResponseDto toExtractResponse(Extract extract){
        return new ExtractResponseDto(
                extract.getValue(),
                extract.getType(),
                extract.getDescription(),
                AccountResponseDto.toAccountResponseDto(extract.getAccount()),
                extract.getDate()
        );
    }

}
