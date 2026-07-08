package com.example.seugoi_back.Chat.entity;

import com.example.seugoi_back.Common.entity.BaseTime;
import com.example.seugoi_back.User.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessage extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_code")
    private ChatRoom chatRoom;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String message;
}
