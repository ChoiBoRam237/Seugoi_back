package com.example.seugoi_back.Login.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Login.dto.KakaoUserInfoResponseDto;
import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.example.seugoi_back.Login.service.KakaoService;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "카카오 사용자 정보 불러오기", description = "카카오 로그인 후 code로 사용자 정보를 가져옵니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "카카오 사용자 정보를 가져왔습니다",
            content = @Content(
                schema = @Schema(
                    implementation = UserResponseDto.class
                )
            )
        )
    })
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws IOException {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        User user = userService.loginOrRegister(userInfo);

        UserResponseDto responseDto =
            UserResponseDto.builder()
                .accessToken(accessToken)
                .userId(user.getId())
                .nickName(user.getNickname())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("카카오 로그인 성공")
                .data(responseDto)
                .build()
        );
    }
}
