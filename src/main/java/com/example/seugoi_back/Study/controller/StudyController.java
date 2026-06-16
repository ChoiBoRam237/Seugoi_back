package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.StudyRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyCreateResponseDto;
import com.example.seugoi_back.Study.entity.Study;
import com.example.seugoi_back.Study.service.StudyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/study")
public class StudyController {
    private final StudyService studyService;

    @Operation(summary = "스터디 생성", description = "스터디를 생성합니다")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "스터디가 생성되었습니다.",
            content = @Content(
                schema = @Schema(
                    implementation = StudyCreateResponseDto.class
                )
            )
        )
    })
    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> generateStudy(@ModelAttribute StudyRequestDto dto) {
        Study study = studyService.generateStudy(dto);

        StudyCreateResponseDto responseDto =
            StudyCreateResponseDto.builder()
                .userId(study.getUser().getId())
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

    // 모든 스터디 조회 API

    // 특정 스터디 조회 API

    // 검색된 스터디 조회 API

    // 현재 진행중인 스터디 조회 API

    // 최근 봤던 스터디 조회 API

    // 찜한 스터디 조회 API

    // 내가 만든 스터디 조회 API

    // 내가 가입한 스터디 조회 API
}
