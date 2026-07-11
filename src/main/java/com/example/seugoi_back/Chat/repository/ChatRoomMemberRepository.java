package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatRoomMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRoomMemberRepository extends JpaRepository<ChatRoomMember, Long> {
    List<ChatRoomMember> findByUser_Code(Long userCode);
    void deleteByChatRoom_Code(Long chatRoomCode);
}
