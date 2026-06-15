package com.example.seugoi_back.Login.controller;

import com.example.seugoi_back.Login.dto.KakaoUserInfoResponseDto;
import com.example.seugoi_back.Login.service.KakaoService;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final UserService userService;
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        User user = userService.loginOrRegister(userInfo);

        return ResponseEntity.ok(
            Map.of(
                "accessToken", accessToken,
                "userId", user.getId(),
                "nickname", user.getNickname(),
                "email", user.getEmail(),
                "profileImageUrl", user.getProfileImageUrl()
            )
        );
    }
}
