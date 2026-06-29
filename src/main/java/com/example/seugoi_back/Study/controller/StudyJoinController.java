package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.entity.StudyJoin;
import com.example.seugoi_back.Study.service.StudyJoinService;
import com.example.seugoi_back.User.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/study/join")
@Tag(name = "Study Join", description = "스터디 가입 관련 API")
public class StudyJoinController {
    private final StudyJoinService studyJoinService;

    @Operation(summary = "스터디 가입 API", description = "스터디를 가입합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "스터디 가입 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PostMapping(value = "")
    public ResponseEntity<?> postJoinStudy(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestParam Long studyCode
    ) {
        StudyJoin studyJoin = studyJoinService.joinStudy(user.getCode(), studyCode);

        CommonStudyResponseDto responseDto =
            CommonStudyResponseDto.builder()
                .code(studyJoin.getCode())
                .userCode(studyJoin.getUser().getCode())
                .studyCode(studyJoin.getStudy().getCode())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("스터디 가입 성공")
                .data(responseDto)
                .build()
        );
    }
}
