package com.example.seugoi_back.Login.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseDto {

    @Schema(name = "accessToken", example = "accessToken")
    private String accessToken;

    @Schema(name = "refreshToken", example = "refreshToken")
    private String refreshToken;

    @Schema(name = "userCode", example = "1")
    private Long userCode;

    @Schema(name = "name", example = "카카오")
    private String name;

    @Schema(name = "email", example = "abc@example.com")
    private String email;

    @Schema(name = "prifileImgUrl", example = "aaa.png")
    private String profileImgUrl;
}
