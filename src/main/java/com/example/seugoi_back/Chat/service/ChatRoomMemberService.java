package com.example.seugoi_back.Chat.service;

import com.example.seugoi_back.Chat.entity.ChatRoom;
import com.example.seugoi_back.Chat.entity.ChatRoomMember;
import com.example.seugoi_back.Chat.repository.ChatRoomMemberRepository;
import com.example.seugoi_back.Chat.repository.ChatRoomRepository;
import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ChatRoomMemberService {
    private final UserRepository userRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;

    @Transactional // 채팅방 가입 Service
    public ChatRoomMember joinChatRoom(Long userCode, Long chatRoomCode) {
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomCode)
            .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        ChatRoomMember chatRoomMember = ChatRoomMember.builder()
            .user(user)
            .chatRoom(chatRoom)
            .build();

        return chatRoomMemberRepository.save(chatRoomMember);
    }

    @Transactional // 채팅방 가입자 삭제 Service
    public void deleteByChatRoomCode(Long chatRoomCode) {
        chatRoomMemberRepository.deleteByChatRoom_Code(chatRoomCode);
    }
}
