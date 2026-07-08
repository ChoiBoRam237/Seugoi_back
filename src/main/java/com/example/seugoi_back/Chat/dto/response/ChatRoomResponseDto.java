package com.example.seugoi_back.Chat.dto.response;

import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ChatRoomResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "user")
    private UserResponseDto user;

    @Schema(name = "study")
    private StudyResponseDto study;

    @Schema(name = "roomName", example = "방 이름")
    private String roomName;
}
