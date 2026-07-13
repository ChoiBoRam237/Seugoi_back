package com.example.seugoi_back.Chat.controller;

import com.example.seugoi_back.Chat.dto.request.ChatRequestDto;
import com.example.seugoi_back.Chat.dto.response.ChatMessageResponseDto;
import com.example.seugoi_back.Chat.dto.response.ChatRoomResponseDto;
import com.example.seugoi_back.Chat.service.ChatImgService;
import com.example.seugoi_back.Chat.service.ChatMessageService;
import com.example.seugoi_back.Chat.service.ChatRoomService;
import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.User.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/chat")
@Tag(name = "Chat", description = "채팅 관련 API")
public class ChatController {
    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;
    private final ChatImgService chatImgService;

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

    @Operation(summary = "특정 채팅방 조회 API", description = "특정 채팅방 정보을 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "특정 채팅방 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = ChatRoomResponseDto.class
                )
            )
        )
    })
    @GetMapping("/room/{chatRoomCode}")
    public ResponseEntity<?> getChatRoom(@PathVariable Long chatRoomCode) {
        ChatRoomResponseDto responseDto = chatRoomService.findByChatRoomCode(chatRoomCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("특정 채팅방 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "채팅 메시지 전송 API", description = "메시지를 전송합니다.")
    @MessageMapping("/send/{chatRoomCode}")
    public void sendMessage(
        @DestinationVariable Long chatRoomCode,
        @Payload ChatRequestDto dto
    ) {
        chatMessageService.sendMessage(chatRoomCode, dto);
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
    @GetMapping("/messages/{chatRoomCode}")
    public ResponseEntity<?> findByChatRoomCode(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
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

    @Operation(summary = "채팅 이미지 파일 저장(DB 저장 X) API", description = "채팅 이미지 파일 저장합니다.(DB 저장에는 저장하지 않습니다.)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "채팅 이미지 파일만 저장 성공",
            content = @Content(
                examples = @ExampleObject(
                    value = """
                    {
                        "imgList": [
                            "aaa.png",
                            "bbb.png"
                        ]
                    }
                    """
                )
            )
        )
    })
    @PostMapping(value = "/save-img", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postChatImgList(@ModelAttribute List<MultipartFile> imgList) {
        List<String> chatImgList = chatImgService.saveImgList(imgList);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("채팅 이미지 파일만 저장 성공")
                .data(chatImgList)
                .build()
        );
    }

    @Operation(summary = "채팅방 탈퇴 API", description = "해당 채팅방을 탈퇴합니다. (유저 정보만 변경되고 실제로 삭제되지는 않습니다.)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "채팅방 탈퇴 성공"
        )
    })
    @DeleteMapping("/room/{chatRoomCode}")
    public ResponseEntity<?> deleteByChatRoomCode(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable Long chatRoomCode
    ) {
        chatRoomService.exitChatRoom(user.getCode(), chatRoomCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("채팅방 탈퇴 성공")
                .build()
        );
    }
}
