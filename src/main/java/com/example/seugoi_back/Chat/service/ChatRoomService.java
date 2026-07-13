package com.example.seugoi_back.Chat.service;

import com.example.seugoi_back.Chat.dto.response.ChatMessageResponseDto;
import com.example.seugoi_back.Chat.dto.response.ChatRoomResponseDto;
import com.example.seugoi_back.Chat.entity.ChatMessage;
import com.example.seugoi_back.Chat.entity.ChatRoom;
import com.example.seugoi_back.Chat.entity.ChatRoomMember;
import com.example.seugoi_back.Chat.enums.ChatMessageType;
import com.example.seugoi_back.Chat.repository.ChatMessageRepository;
import com.example.seugoi_back.Chat.repository.ChatRoomMemberRepository;
import com.example.seugoi_back.Chat.repository.ChatRoomRepository;
import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.Study.service.StudyBgImgService;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {
    private final SimpMessagingTemplate simpMessagingTemplate;

    private final UserRepository userRepository;
    private final StudyRepository studyRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMessageService chatMessageService;
    private final ChatRoomMemberService chatRoomMemberService;
    private final StudyBgImgService studyBgImgService;

    @Transactional // 채팅방 생성 Service
    public ChatRoom generateChatRoom(Long userCode, Long studyCode, String roomName) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        Study study = studyRepository.findById(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.STUDY_NOT_FOUND));

        // 채팅방 생성
        ChatRoom chatRoom = ChatRoom.builder()
            .user(user)
            .study(study)
            .roomName(roomName)
            .build();
        ChatRoom savedChatRoom = chatRoomRepository.save(chatRoom);

        // 관리자 채팅방 가입
        chatRoomMemberService.joinChatRoom(userCode, savedChatRoom.getCode());

        return savedChatRoom;
    }

    @Transactional // 내가 참여되어 있는 채팅방 목록 조회 (검색 가능) Service
    public List<ChatRoomResponseDto> findByJoinAndKeyword(Long userCode, String keyword) {
        List<ChatRoomMember> chatRoomMemberList = chatRoomMemberService.findByUserCode(userCode);

        List<ChatRoomResponseDto> responseDto = chatRoomMemberList.stream()
                .filter(ChatRoomMember::isJoined)
                .map(ChatRoomMember::getChatRoom)
                .filter(chatRoom -> keyword == null
                    || keyword.isBlank()
                    || chatRoom.getRoomName().toLowerCase().contains(keyword.toLowerCase()))
                .map(chatRoom -> {
                    ChatMessageResponseDto lastMessage = chatMessageService.findLastMessage(chatRoom.getCode());

                    return ChatRoomResponseDto.builder()
                            .code(chatRoom.getCode())
                            .roomName(chatRoom.getRoomName())
                            .lastMessage(lastMessage != null ? lastMessage.getMessage() : null)
                            .lastMessageDate(lastMessage != null ? lastMessage.getCreatedAt() : null)
                            .unreadMessageCount(chatMessageService.unreadMessageCount(userCode, chatRoom.getCode()))
                            .study(
                                StudyResponseDto.builder()
                                    .code(chatRoom.getStudy().getCode())
                                    .bgImg(studyBgImgService.findByStudyCode(chatRoom.getStudy().getCode()))
                                    .build()
                            )
                            .build();
                })
                .toList();

        return responseDto;
    }

    @Transactional // 특정 채팅방 조회 Service
    public ChatRoomResponseDto findByChatRoomCode(Long chatRoomCode) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        ChatRoomResponseDto responseDto = ChatRoomResponseDto.builder()
            .code(chatRoom.getCode())
            .roomName(chatRoom.getRoomName())
            .study(
                StudyResponseDto.builder()
                    .code(chatRoom.getStudy().getCode())
                    .bgImg(studyBgImgService.findByStudyCode(chatRoom.getStudy().getCode()))
                    .build()
            )
            .build();

        return responseDto;
    }

    @Transactional // 채팅방 나가기 Service
    public void exitChatRoom(Long userCode, Long chatRoomCode) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));
        ChatRoomMember chatRoomMember = chatRoomMemberRepository.findByUser_CodeAndChatRoom_Code(userCode, chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_MEMBER_NOT_FOUND));

        // 퇴장 메시지 저장
        ChatMessage chatMessage = ChatMessage.builder()
            .user(
                User.builder()
                    .code(user.getCode())
                    .name("알 수 없음")
                    .profileImgUrl(null)
                    .build()
            )
            .chatRoom(chatRoom)
            .type(ChatMessageType.LEAVE)
            .message(user.getName() + "님이 퇴장하셨습니다")
            .build();
        ChatMessage saved = chatMessageRepository.save(chatMessage);
        chatRoomMember.setJoined(false);
        simpMessagingTemplate.convertAndSend("/sub/room/" + chatRoomCode, ChatMessageResponseDto.from(saved));

        // 해당 채팅방에서 유저가 보낸 모든 채팅 메시지 유저 정보 변경
        chatRoomRepository.anonymizeMessages(userCode, chatRoomCode);
    }

    @Transactional // 채팅방 삭제 Service
    public void deleteByStudyCode(Long studyCode) {
        ChatRoom chatRoom = chatRoomRepository.findByStudy_Code(studyCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        chatMessageService.deleteByChatRoomCode(chatRoom.getCode()); // 메시지 삭제
        chatRoomMemberService.deleteByChatRoomCode(chatRoom.getCode()); // 가입자 삭제
        chatRoomRepository.deleteById(chatRoom.getCode()); // 채팅방 삭제
    }
}
