package com.example.seugoi_back.Chat.dto.response;

import com.example.seugoi_back.Chat.entity.ChatMessage;
import com.example.seugoi_back.Chat.enums.ChatMessageType;
import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ChatMessageResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "user")
    private UserResponseDto user;

    @Schema(name = "type", example = "CHAT")
    private ChatMessageType type;

    @Schema(name = "message", example = "채팅 메시지")
    private String message;

    @Schema(name = "imgList")
    private List<String> imgList;

    @Schema(name = "owner", example = "true")
    private boolean owner; // 스터디 관리자인지 아닌지

    @Schema(name = "senderName", example = "스프링")
    private String senderName;

    @Schema(name = "senderProfileImgUrl", example = "aaa.png")
    private String senderProfileImgUrl;

    @Schema(name = "createdAt", example = "2026-01-01")
    private LocalDateTime createdAt;

    public static ChatMessageResponseDto from(ChatMessage chatMessage) {
        return ChatMessageResponseDto.builder()
            .code(chatMessage.getCode())
            .user(
                UserResponseDto.builder()
                    .userCode(chatMessage.getUser().getCode())
                    .name(chatMessage.getUser().getName())
                    .profileImgUrl(chatMessage.getUser().getProfileImgUrl())
                    .build()
            )
            .owner(false)
            .type(chatMessage.getType())
            .message(chatMessage.getMessage())
            .imgList(Collections.emptyList())
            .createdAt(chatMessage.getCreatedAt())
            .build();
    }
}
