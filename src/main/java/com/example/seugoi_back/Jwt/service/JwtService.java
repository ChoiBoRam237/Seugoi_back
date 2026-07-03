package com.example.seugoi_back.Jwt.service;

import com.example.seugoi_back.Jwt.JwtTokenProvider;
import com.example.seugoi_back.Jwt.entity.RefreshToken;
import com.example.seugoi_back.Jwt.repository.RefreshTokenRepository;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtTokenProvider jwtTokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Transactional // 토큰 발급 Service
    public Map<String, Object> createToken(Long userCode, String userEmail) {
        // 엑세스 토큰 발급
        String accessToken = jwtTokenProvider.createAccessToken(userCode, userEmail);

        // 리프레스 토큰 발급
        String refreshToken = jwtTokenProvider.createRefreshToken(userCode);

        refreshTokenRepository.save(
            new RefreshToken(
                userCode,
                refreshToken,
                LocalDateTime.now().plusDays(14)
            )
        );

        return Map.of(
            "accessToken", accessToken,
            "refreshToken", refreshToken
        );
    }

    @Transactional // 엑세스 토큰 재발급 Service
    public String refreshAccessToken(String refreshToken) {
        if (!jwtTokenProvider.validateToken(refreshToken)) {
            throw new RuntimeException("유효하지 않는 토큰입니다.");
        }

        Long userCode = jwtTokenProvider.getUserCode(refreshToken);
        User user = userRepository.findById(userCode)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        RefreshToken refresh = refreshTokenRepository.findById(userCode)
            .orElseThrow(() -> new RuntimeException("리프레시 토큰을 찾을 수 없습니다."));

        if (refresh.getRefreshToken() == null || !refresh.getRefreshToken().equals(refreshToken)) {
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }

        String newAccessToken =
            jwtTokenProvider.createAccessToken(
                user.getCode(),
                user.getEmail()
            );

        return newAccessToken;
    }
}
