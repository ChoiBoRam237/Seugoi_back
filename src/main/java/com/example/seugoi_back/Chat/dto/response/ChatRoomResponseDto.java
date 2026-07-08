package com.example.seugoi_back.Chat.dto.response;

import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ChatRoomResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "study")
    private StudyResponseDto study;

    @Schema(name = "roomName", example = "방 이름")
    private String roomName;

    @Schema(name = "lastMessage", example = "마지막 메시지")
    private String lastMessage;

    @Schema(name = "lastMessageDate", example = "2026-01-01")
    private LocalDateTime lastMessageDate;
}
