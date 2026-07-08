package com.example.seugoi_back.Chat.repository;

import com.example.seugoi_back.Chat.entity.ChatImg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatImgRepository extends JpaRepository<ChatImg, Long> {
    List<ChatImg> findByChatMessage_Code(Long chatMessageCode);
}
