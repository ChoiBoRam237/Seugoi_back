package com.example.seugoi_back.User.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.User.entity.User;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/user")
@Tag(name = "User", description = "유저 관련 API")
public class UserController {
    private final UserService userService;

    @Operation(summary = "특정 유저 정보 조회 API", description = "유저 정보를 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "특정 유저 정보 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = User.class
                )
            )
        )
    })
    @GetMapping("/{userCode}")
    public ResponseEntity<?> getUser(@PathVariable Long userCode) {
        User user = userService.findUserByCode(userCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("특정 유저 정보 조회 성공")
                .data(user)
                .build()
        );
    }
}
