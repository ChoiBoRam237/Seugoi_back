package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatMessage;
import com.example.seugoi_back.Chat.enums.ChatMessageType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_Code(Long chatRoomCode);
    Optional<ChatMessage> findFirstByChatRoom_CodeAndTypeOrderByCreatedAtDesc(Long chatRoomCode, ChatMessageType type);
    Long countByChatRoom_CodeAndCodeGreaterThanAndType(Long chatRoomCode, Long chatMessageCode, ChatMessageType type);
    void deleteByChatRoom_Code(Long chatRoomCode);
}
