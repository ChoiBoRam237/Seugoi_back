package com.example.seugoi_back.Login.controller;

import com.example.seugoi_back.Common.exception.CustomException;
import com.example.seugoi_back.Common.exception.ErrorCode;
import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Jwt.JwtTokenProvider;
import com.example.seugoi_back.Jwt.entity.RefreshToken;
import com.example.seugoi_back.Jwt.repository.RefreshTokenRepository;
import com.example.seugoi_back.Jwt.service.JwtService;
import com.example.seugoi_back.Login.dto.KakaoTokenResponseDto;
import com.example.seugoi_back.Login.dto.KakaoUserInfoResponseDto;
import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.example.seugoi_back.Login.service.KakaoService;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.User.repository.UserRepository;
import com.example.seugoi_back.User.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/kakao")
@Tag(name = "Kakao", description = "Kakao 관련 API")
public class KakaoLoginController {
    private final KakaoService kakaoService;
    private final UserService userService;
    private final JwtService jwtService;

    private final UserRepository userRepository;

    @Operation(summary = "카카오 사용자 정보 조회 API", description = "카카오 로그인 후 code로 사용자 정보를 가져옵니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
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
        User user;

        // 유저 정보가 DB에 저장되어 있으면 새로 저장하지 않고 바로 유저 정보 전달
        if (userRepository.findByKakaoId(userInfo.getId()).isPresent()) {
            user = userRepository.findByKakaoId(userInfo.getId())
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
        } else {
            user = userService.loginOrRegister(userInfo);
        }

        Map<String, Object> token = jwtService.createToken(user.getCode(), user.getEmail());

        UserResponseDto responseDto =
            UserResponseDto.builder()
                .accessToken(token.get("accessToken").toString())
                .refreshToken(token.get("refreshToken").toString())
                .userCode(user.getCode())
                .name(user.getName())
                .email(user.getEmail())
                .profileImgUrl(user.getProfileImgUrl())
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
