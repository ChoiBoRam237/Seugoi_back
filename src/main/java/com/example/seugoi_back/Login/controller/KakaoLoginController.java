package com.example.seugoi_back.Login.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Login.dto.KakaoTokenResponseDto;
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
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/kakao")
public class KakaoLoginController {

    private final KakaoService kakaoService;
    private final UserService userService;

    @Operation(summary = "카카오 사용자 정보 조회 API", description = "카카오 로그인 후 code로 사용자 정보를 가져옵니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "카카오 로그인 성공",
            content = @Content(
                schema = @Schema(
                    implementation = UserResponseDto.class
                )
            )
        )
    })
    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) throws IOException {
        KakaoTokenResponseDto tokenData = kakaoService.getAccessTokenFromKakao(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(tokenData.getAccessToken());
        User user = userService.loginOrRegister(userInfo);

        UserResponseDto responseDto =
            UserResponseDto.builder()
                .accessToken(tokenData.getAccessToken())
                .refreshToken(tokenData.getRefreshToken())
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

    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "액세스 토큰 갱신 API", description = "refresh token으로 access token을 갱신합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "액세스 토큰 갱신 성공",
            content = @Content(
                schema = @Schema(
                    implementation = KakaoTokenResponseDto.class
                )
            )
        )
    })
    @GetMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestHeader("Authorization") String refreshToken) throws IOException {
        KakaoTokenResponseDto tokenData = kakaoService.getRefreshToke(refreshToken);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("액세스 토큰 갱신 성공")
                .data(tokenData)
                .build()
        );
    }
}
