package com.swiss.wallet.web.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.swiss.wallet.entity.Extract;
import com.swiss.wallet.entity.UserEntity;
import jakarta.validation.constraints.Pattern;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public record ExtractResponseDto(Long id,
                                 Double value,
                                 Extract.Type type,
                                 String description,
                                 @JsonFormat(pattern = "dd/MM/yyyy - HH:mm")
                                 LocalDateTime date
                                 ) {

    public static ExtractResponseDto toExtractResponse(Extract extract){
        return new ExtractResponseDto(
                extract.getId(),
                extract.getValue(),
                extract.getType(),
                extract.getDescription(),
                extract.getDate()
        );
    }

    public static List<ExtractResponseDto> toListExtractResponse(List<Extract> extracts){
        return extracts.stream()
                .map(extract -> toExtractResponse(extract)).collect(Collectors.toList());
    }

}
