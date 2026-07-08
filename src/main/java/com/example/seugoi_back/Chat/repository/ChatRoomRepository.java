package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByStudy_Code(Long studyCode);
}
