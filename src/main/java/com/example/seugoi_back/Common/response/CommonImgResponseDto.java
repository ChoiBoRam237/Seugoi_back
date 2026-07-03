package com.example.seugoi_back.Common.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonImgResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "folderName", example = "/uploads")
    private String folderName;

    @Schema(name = "imgUrl", example = "aaa.png")
    private String imgUrl;
}
