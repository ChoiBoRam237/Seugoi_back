package com.example.seugoi_back.Chat.enums;

public enum ChatMessageType {
    CHAT("일반 채팅"),
    JOIN("입장"),
    LEAVE("퇴장");

    private final String description;

    ChatMessageType(String description) {
        this.description = description;
    }
}
