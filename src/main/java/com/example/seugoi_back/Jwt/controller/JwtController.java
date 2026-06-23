package com.example.seugoi_back.Jwt.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Jwt.service.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/token")
@Tag(name = "Token", description = "Token 관련 API")
public class JwtController {
    private final JwtService jwtService;

    @SecurityRequirement(name = "BearerAuth")
    @Operation(summary = "액세스 토큰 갱신 API", description = "refresh token으로 access token을 갱신합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "액세스 토큰 갱신 성공",
            content = @Content(
                examples = @ExampleObject(
                    value = """
                    {
                        "accessToken": "agdaaddfajfd="
                    }
                    """
                )
            )
        )
    })
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@RequestBody String refreshToken) {
        String newAccessToken = jwtService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("엑세스 토큰 재발급 성공")
                .data(newAccessToken)
                .build()
        );
    }
}
