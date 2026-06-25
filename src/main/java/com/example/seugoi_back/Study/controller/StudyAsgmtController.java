package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.StudyAsgmtRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.entity.StudyAsgmt;
import com.example.seugoi_back.Study.service.StudyAsgmtService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/study/asgmt")
@Tag(name = "Study Assignment", description = "스터디 과제 관련 API")
public class StudyAsgmtController {
    private final StudyAsgmtService studyAsgmtService;

    @Operation(summary = "스터디 과제 생성 API", description = "스터디 과제를 생성합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "스터디 과제 생성 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postGenerateStudyAsgmt(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam Long studyCode,
        @Valid @ModelAttribute StudyAsgmtRequestDto dto
    ) {
        StudyAsgmt studyAsgmt = studyAsgmtService.generateStudyAsgmt(user.getCode(), studyCode, dto);

        CommonStudyResponseDto responseDto =
            CommonStudyResponseDto.builder()
                .code(studyAsgmt.getCode())
                .userCode(studyAsgmt.getUser().getCode())
                .studyCode(studyAsgmt.getStudy().getCode())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("스터디 과제 생성 성공")
                .data(responseDto)
                .build()
        );
    }
}
