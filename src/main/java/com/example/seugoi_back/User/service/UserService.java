package com.example.seugoi_back.User.service;

import com.example.seugoi_back.Login.dto.KakaoUserInfoResponseDto;
import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional // 회원가입
    public User loginOrRegister(KakaoUserInfoResponseDto userInfo) {
        return userRepository
            .findById(userInfo.getId())
            .orElseGet(() -> {
                User user = User.builder()
                    .kakaoId(String.valueOf(userInfo.getId()))
                    .email(userInfo.getKakaoAccount().getEmail())
                    .nickname(userInfo.getKakaoAccount().getProfile().getNickName())
                    .profileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                    .build();

                return userRepository.save(user);
            });
    }

    @Transactional // 특정 유저 조회 Service
    public User findByUserCode(Long userCode) {
        return userRepository.findById(userCode)
            .orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
    }
}
