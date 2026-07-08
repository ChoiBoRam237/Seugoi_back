package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {
    List<ChatMessage> findByChatRoom_Code(Long chatRoomCode);
    void deleteByChatRoom_Code(Long chatRoomCode);
}
