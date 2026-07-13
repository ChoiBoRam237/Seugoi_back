package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatRoomMember;
import com.example.seugoi_back.Chat.enums.ChatMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByUser_Code(Long userCode);
    Optional<ChatRoomMember> findByUser_CodeAndChatRoom_Code(Long userCode, Long chatRoomCode);
    Long countByChatRoom_CodeAndCodeGreaterThanAndType(Long chatRoomCode, Long lastReadMessageCode, ChatMessageType type);
    void deleteByChatRoom_Code(Long chatRoomCode);
}
