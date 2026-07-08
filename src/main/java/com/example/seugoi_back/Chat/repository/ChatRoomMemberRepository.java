package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    void deleteByChatRoom_Code(Long chatRoomCode);
}
