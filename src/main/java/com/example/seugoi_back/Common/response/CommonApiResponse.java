package com.example.seugoi_back.Common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonApiResponse<T> {

    @Schema(example = "true")
    private boolean success;

    @Schema(example = "요청 성공")
    private String message;

    private T data;
}
