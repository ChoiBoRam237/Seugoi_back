package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyCreateResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.service.StudyService;
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
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/study")
@Tag(name = "Study", description = "스터디 관련 API")
public class StudyController {
    private final StudyService studyService;

    @Operation(summary = "스터디 생성 API", description = "스터디를 생성합니다")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "스터디 생성 성공",
            content = @Content(
                schema = @Schema(
                    implementation = StudyCreateResponseDto.class
                )
            )
        )
    })
    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postGenerateStudy(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @ModelAttribute StudyRequestDto dto
    ) {
        Study study = studyService.generateStudy(user.getCode(), dto);

        StudyCreateResponseDto responseDto =
            StudyCreateResponseDto.builder()
                .userCode(study.getUser().getCode())
                .code(study.getCode())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("스터디 생성 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "모든 스터디 조회 API", description = "생성된 모든 스터디를 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "모든 스터디 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = StudyResponseDto.class
                )
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<?> getStudyAll(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestParam(defaultValue = "ALL") String filterValue,
            @RequestParam(defaultValue = "LATEST") String sortValue
    ) {
        List<StudyResponseDto> responseDto = studyService.findStudyAll(user.getCode(), filterValue, sortValue);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("모든 스터디 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "특정 스터디 상세 조회 API", description = "특정 스터디를 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "특정 스터디 상세 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = Study.class
                )
            )
        )
    })
    @GetMapping("/{studyCode}")
    public ResponseEntity<?> getStudyById(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @PathVariable String studyCode
    ) {
        Map<String, Object> responseDto = studyService.findStudyByCode(user.getCode(), Long.valueOf(studyCode));

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("특정 스터디 상세 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "스터디 검색 API", description = "스터디를 검색합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "검색한 스터디 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = Study.class
                )
            )
        )
    })
    @GetMapping("/search")
    public ResponseEntity<?> getStudySearch(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam String keyword
    ) {
        List<StudyResponseDto> studyList = studyService.findStudyByKeyword(user.getCode(), keyword);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("검색한 스터디 조회 성공")
                .data(studyList)
                .build()
        );
    }
}
