package com.example.seugoi_back.Chat.dto.response;

import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "user")
    private UserResponseDto user;

    @Schema(name = "message", example = "채팅 메시지")
    private String message;

    @Schema(name = "imgList")
    private List<CommonImgResponseDto> imgList;

    @Schema(name = "isMine", example = "true")
    private boolean isMine; // 해당 메시지를 내가 보냈는지 아닌지

    @Schema(name = "createdAt", example = "2026-01-01")
    private LocalDateTime createdAt;
}
