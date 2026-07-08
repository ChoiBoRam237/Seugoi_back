package com.example.seugoi_back.Chat.service;

import com.example.seugoi_back.Chat.dto.request.ChatRequestDto;
import com.example.seugoi_back.Chat.dto.response.ChatMessageResponseDto;
import com.example.seugoi_back.Chat.entity.ChatImg;
import com.example.seugoi_back.Chat.entity.ChatMessage;
import com.example.seugoi_back.Chat.entity.ChatRoom;
import com.example.seugoi_back.Chat.repository.ChatImgRepository;
import com.example.seugoi_back.Chat.repository.ChatMessageRepository;
import com.example.seugoi_back.Chat.repository.ChatRoomRepository;
import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class ChatMessageService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatImgRepository chatImgRepository;
    private final ChatImgService chatImgService;

    @Transactional // 메시지 전송 Service
    public void sendMessage(Long userCode, Long chatRoomCode, ChatRequestDto dto) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 채팅 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
            .user(user)
            .chatRoom(chatRoom)
            .message(dto.getMessage())
            .build();
        ChatMessage savedMessage = chatMessageRepository.save(chatMessage);

        // 채팅 이미지가 있을 때만 실행
        if (dto.getImgList() != null && !dto.getImgList().isEmpty()) {
            // 채팅 이미지 저장
            List<String> chatImgList = chatImgService.savedChatImg(dto.getImgList());
            for (String img : chatImgList) {
                ChatImg chatImg = ChatImg.builder()
                    .user(user)
                    .chatMessage(savedMessage)
                    .folderName("/uploads/chat/")
                    .imgUrl(img)
                    .build();
                chatImgRepository.save(chatImg);
            }
        }

        // 메시지 전송
        ChatMessageResponseDto responseDto =
            ChatMessageResponseDto.builder()
                .code(chatMessage.getCode())
                .user(
                    UserResponseDto.builder()
                        .userCode(user.getCode())
                        .name(user.getName())
                        .profileImgUrl(user.getProfileImgUrl())
                        .build()
                )
                .isMine(Objects.equals(chatMessage.getUser().getCode(), userCode))
                .message(chatMessage.getMessage())
                .imgList(chatImgService.findByChatMessageCode(chatMessage.getCode()))
                .createdAt(chatMessage.getCreatedAt())
                .build();
        simpMessagingTemplate.convertAndSend("/chat/room" + chatRoomCode, responseDto);
    }

    @Transactional // 메시지 조회 Service
    public List<ChatMessageResponseDto> findByChatRoomCode(Long userCode, Long chatRoomCode) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        List<ChatMessage> chatMessage = chatMessageRepository.findByChatRoom_Code(chatRoomCode);

        List<ChatMessageResponseDto> responseDto = chatMessage.stream()
            .map(item -> ChatMessageResponseDto.builder()
                .code(item.getCode())
                .user(
                    UserResponseDto.builder()
                        .userCode(user.getCode())
                        .name(user.getName())
                        .profileImgUrl(user.getProfileImgUrl())
                        .build()
                )
                .isMine(Objects.equals(item.getUser().getCode(), userCode))
                .message(item.getMessage())
                .imgList(chatImgService.findByChatMessageCode(item.getCode()))
                .createdAt(item.getCreatedAt())
                .build())
            .sorted(Comparator.comparing(ChatMessageResponseDto::getCreatedAt))
            .toList();

        return responseDto;
    }

    @Transactional // 마지막 메시지 조회 Service
    public ChatMessageResponseDto findLastMessage(Long chatRoomCode) {
        return chatMessageRepository
            .findFirstByChatRoom_CodeOrderByCreatedAtDesc(chatRoomCode)
            .map(chatMessage -> ChatMessageResponseDto.builder()
                .message(chatMessage.getMessage())
                .createdAt(chatMessage.getCreatedAt())
                .build())
                .orElse(
                    ChatMessageResponseDto.builder()
                        .message("")
                        .createdAt(null)
                        .build()
                );
    }

    @Transactional // 채팅방 code에 맞는 모든 메시지 삭제
    public void deleteByChatRoomCode(Long chatRoomCode) {
        List<ChatMessage> chatMessage = chatMessageRepository.findByChatRoom_Code(chatRoomCode);

        // 메시지가 있으면 실행
        if (chatMessage != null && !chatMessage.isEmpty()) {
            chatMessage.forEach(message ->
                    chatImgService.deleteByChatMessageCode(message.getCode()) // 이미지 삭제
            );
            chatMessageRepository.deleteByChatRoom_Code(chatRoomCode); // 메시지 삭제
        }
    }
}
