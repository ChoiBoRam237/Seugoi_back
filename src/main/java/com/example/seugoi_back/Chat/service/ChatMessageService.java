package com.example.seugoi_back.Chat.service;

import com.example.seugoi_back.Chat.dto.request.ChatRequestDto;
import com.example.seugoi_back.Chat.dto.response.ChatMessageResponseDto;
import com.example.seugoi_back.Chat.entity.ChatImg;
import com.example.seugoi_back.Chat.entity.ChatMessage;
import com.example.seugoi_back.Chat.entity.ChatRoom;
import com.example.seugoi_back.Chat.entity.ChatRoomMember;
import com.example.seugoi_back.Chat.enums.ChatMessageType;
import com.example.seugoi_back.Chat.repository.ChatImgRepository;
import com.example.seugoi_back.Chat.repository.ChatMessageRepository;
import com.example.seugoi_back.Chat.repository.ChatRoomMemberRepository;
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
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatImgRepository chatImgRepository;
    private final ChatImgService chatImgService;
    private final ChatRoomMemberService chatRoomMemberService;

    @Transactional // 메시지 전송 Service
    public void sendMessage(Long chatRoomCode, ChatRequestDto dto) {
        User user = userRepository.findById(dto.getUserCode())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        // 채팅 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
            .user(user)
            .chatRoom(chatRoom)
            .senderName(user.getName())
            .senderProfileImgUrl(user.getProfileImgUrl())
            .type(ChatMessageType.CHAT)
            .message(dto.getMessage())
            .build();
        chatMessageRepository.save(chatMessage);

        // 이미지 DB 저장
        if (dto.getImgList() != null && !dto.getImgList().isEmpty()) {
            for (String img : dto.getImgList()) {
                ChatImg chatImg = ChatImg.builder()
                    .user(user)
                    .chatMessage(chatMessage)
                    .folderName("/uploads/chat/")
                    .imgUrl(img)
                    .build();
                chatImgRepository.save(chatImg);
            }
        }

        // 마지막으로 읽은 메시지 업데이트
        chatRoomMemberService.updateLastReadMessage(dto.getUserCode(), chatRoomCode, chatMessage.getCode());

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
                .owner(Objects.equals(chatRoom.getStudy().getUser().getCode(), chatMessage.getUser().getCode()))
                .senderName(user.getName())
                .senderProfileImgUrl(user.getProfileImgUrl())
                .type(chatMessage.getType())
                .message(chatMessage.getMessage())
                .imgList(
                    dto.getImgList().stream()
                        .map(img -> "/uploads/chat/" + img)
                        .toList()
                )
                .createdAt(chatMessage.getCreatedAt())
                .build();
        simpMessagingTemplate.convertAndSend("/sub/room/" + chatRoomCode, responseDto);
    }

    @Transactional // 이전 채팅 내용 조회 Service
    public List<ChatMessageResponseDto> findByChatRoomCode(Long userCode, Long chatRoomCode) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        List<ChatMessage> chatMessage = chatMessageRepository.findByChatRoom_Code(chatRoomCode);

        // 마지막으로 읽은 메시지 업데이트
        if (!chatMessage.isEmpty()) {
            Long lastMessageCode = chatMessage.get(chatMessage.size() - 1).getCode();

            chatRoomMemberService.updateLastReadMessage(userCode, chatRoomCode, lastMessageCode);
        }

        List<ChatMessageResponseDto> responseDto = chatMessage.stream()
            .map(item -> ChatMessageResponseDto.builder()
                .code(item.getCode())
                .user(
                    UserResponseDto.builder()
                        .userCode(item.getUser().getCode())
                        .name(item.getUser().getName())
                        .profileImgUrl(item.getUser().getProfileImgUrl())
                        .build()
                )
                .senderName(item.getSenderName())
                .senderProfileImgUrl(item.getSenderProfileImgUrl())
                .owner(Objects.equals(item.getUser().getCode(), chatRoom.getUser().getCode()))
                .type(item.getType())
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
            .findFirstByChatRoom_CodeAndTypeOrderByCreatedAtDesc(chatRoomCode, ChatMessageType.CHAT)
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

    @Transactional // 안 읽은 메시지 개수 조회 Service
    public Long unreadMessageCount(Long userCode, Long chatRoomCode) {
        List<ChatMessage> chatMessageList = chatMessageRepository.findByChatRoom_Code(chatRoomCode);
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByUser_CodeAndChatRoom_Code(userCode, chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_MEMBER_NOT_FOUND));

        Long lastReadMessageCode = chatRoomMember.getLastReadMessageCode();

        // 한 번도 읽지 않은 경우
        if (lastReadMessageCode == null) {
            return (long) chatMessageList.stream()
                            .filter(item -> item.getType() == ChatMessageType.CHAT)
                            .toList()
                            .size();
        }

        return chatMessageRepository.countByChatRoom_CodeAndCodeGreaterThanAndType(
            chatRoomCode,
            lastReadMessageCode,
            ChatMessageType.CHAT
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
