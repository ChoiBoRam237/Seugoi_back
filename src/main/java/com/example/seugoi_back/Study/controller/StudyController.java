package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyCreateResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyDetailResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.repository.StudyRepository;
import com.example.seugoi_back.Study.service.StudyService;
import com.example.seugoi_back.User.entity.User;
import com.example.seugoi_back.Util.DateUtil;
import com.example.seugoi_back.Util.ListUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/study")
public class StudyController {

    private final StudyService studyService;

    private final StudyRepository studyRepository;

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
    public ResponseEntity<?> postGenerateStudy(@ModelAttribute StudyRequestDto dto) {
        Study study = studyService.generateStudy(dto);

        StudyCreateResponseDto responseDto =
            StudyCreateResponseDto.builder()
                .userCode(study.getUser().getId())
                .id(study.getId())
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
    public ResponseEntity<?> getStudyAll() {
        List<StudyResponseDto> responseDto = studyService.findStudyAll();

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
    @GetMapping("/{studyId}")
    public ResponseEntity<?> getStudyById(@PathVariable String studyId) {
        Map<String, Object> responseDto = studyService.findStudyById(Long.valueOf(studyId));

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("특정 스터디 상세 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    // 검색된 스터디 조회 API

    // 현재 진행중인 스터디 조회 API

    // 최근 봤던 스터디 조회 API

    // 찜한 스터디 조회 API

    // 내가 만든 스터디 조회 API

    // 내가 가입한 스터디 조회 API
}
