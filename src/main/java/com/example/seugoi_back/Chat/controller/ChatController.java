package com.example.seugoi_back.Chat.controller;

import com.example.seugoi_back.Chat.dto.request.ChatRequestDto;
import com.example.seugoi_back.Chat.dto.response.ChatMessageResponseDto;
import com.example.seugoi_back.Chat.dto.response.ChatRoomResponseDto;
import com.example.seugoi_back.Chat.service.ChatMessageService;
import com.example.seugoi_back.Chat.service.ChatRoomService;
import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.User.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/chat")
@Tag(name = "Chat", description = "채팅 관련 API")
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    @Operation(summary = "내가 가입된 채팅방 목록 조회 (검색 가능) API", description = "내가 가입된 채팅방 목록을 조회합니다. (검색 가능)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "내가 가입된 채팅방 목록 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = ChatRoomResponseDto.class
                )
            )
        )
    })
    @GetMapping("/room")
    public ResponseEntity<?> getChatRoomAll(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam(defaultValue = "") String keyword
    ) {
        List<ChatRoomResponseDto> responseDto = chatRoomService.findByJoinAndKeyword(user.getCode(), keyword);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("내가 가입된 채팅방 목록 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "채팅 메시지 전송 API", description = "메시지를 전송합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "채팅 메시지 전송 성공"
        )
    })
    @MessageMapping("/send")
    @SendTo("/message")
    public ResponseEntity<?> sendMessage(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @DestinationVariable Long chatRoomCode,
        @Payload ChatRequestDto dto
    ) {
        chatMessageService.sendMessage(user.getCode(), chatRoomCode, dto);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("채팅 메시지 전송 성공")
                .build()
        );
    }

    @Operation(summary = "이전 채팅 내용 조회 API", description = "이전 채팅 내용을 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "이전 채팅 내용 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = ChatMessageResponseDto.class
                )
            )
        )
    })
    @GetMapping("/{chatRoomCode}")
    public ResponseEntity<?> findByChatRoomCode(
        @Parameter(hidden = true) User user,
        @PathVariable Long chatRoomCode
    ) {
        List<ChatMessageResponseDto> responseDto = chatMessageService.findByChatRoomCode(user.getCode(), chatRoomCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("이전 채팅 내용 조회 성공")
                .data(responseDto)
                .build()
        );
    }
}
