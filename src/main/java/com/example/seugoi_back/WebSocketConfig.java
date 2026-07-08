package com.example.seugoi_back;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // 서버 -> 클라이언트
        config.enableSimpleBroker("/sub"); // 메시지 받기 위한 구독 주소 prefix

        // 클라이언트 -> 서버
        config.setApplicationDestinationPrefixes("/pub"); // 메시지 보낼 prefix
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws")
            .setAllowedOriginPatterns("*") // cors 허용
            .withSockJS(); // web socket을 지원하지 않는 브라우저를 위해 SockJS
    }
}
