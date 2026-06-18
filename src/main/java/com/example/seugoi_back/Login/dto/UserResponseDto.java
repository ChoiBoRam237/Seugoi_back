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

    @Schema(name = "userId", example = "1")
    private Long userId;

    @Schema(name = "nickName", example = "카카오")
    private String nickName;

    @Schema(name = "email", example = "abc@example.com")
    private String email;

    @Schema(name = "prifileImageUrl", example = "aaa.png")
    private String profileImageUrl;
}
