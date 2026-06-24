package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.StudyNoticeRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.entity.StudyNotice;
import com.example.seugoi_back.Study.service.StudyAsgmtService;
import com.example.seugoi_back.Study.service.StudyNoticeService;
import com.example.seugoi_back.User.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/study/notice")
@Tag(name = "Study Notice", description = "스터디 공지 관련 API")
public class StudyNoticeController {
    private final StudyNoticeService studyNoticeService;

    @Operation(summary = "스터디 공지 생성 API", description = "스터디 공지를 생성합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "스터디 공지 생성 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PostMapping("/generate")
    public ResponseEntity<?> postGenerateStudyNotice(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @Parameter Long studyCode,
        @Valid StudyNoticeRequestDto dto
    ) {
        StudyNotice studyNotice = studyNoticeService.generateStudyNotice(user.getCode(), studyCode, dto);

        CommonStudyResponseDto responseDto =
            CommonStudyResponseDto.builder()
                .code(studyNotice.getCode())
                .userCode(studyNotice.getUser().getCode())
                .studyCode(studyNotice.getStudy().getCode())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("스터디 공지 생성 성공")
                .data(responseDto)
                .build()
        );
    }
}
