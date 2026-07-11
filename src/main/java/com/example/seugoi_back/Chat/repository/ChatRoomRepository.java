package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByStudy_Code(Long studyCode);

    @Modifying
    @Query("""
        update ChatMessage c
        set c.senderName = '알 수 없음',
            c.senderProfileImgUrl = null
        where c.chatRoom.code = :chatRoomCode
            and c.user.code = :userCode
    """)
    void anonymizeMessages(Long userCode, Long chatRoomCode);
}
