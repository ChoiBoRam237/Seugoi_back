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
public class ChatImg extends BaseTime {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long code;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_code")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_message_code")
    private ChatMessage chatMessage;

    @Column(nullable = false)
    private String folderName;

    @Column(nullable = false)
    private String imgUrl;
}
