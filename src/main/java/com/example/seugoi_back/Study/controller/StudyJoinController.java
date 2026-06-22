package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.StudyJoinRequestDto;
import com.example.seugoi_back.Study.dto.response.StudyJoinResponseDto;
import com.example.seugoi_back.Study.entity.StudyJoin;
import com.example.seugoi_back.Study.service.StudyJoinService;
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

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/study/join")
@Tag(name = "Study Join", description = "스터디 가입 관련 API")
public class StudyJoinController {
    private final StudyJoinService studyJoinService;

    @Operation(summary = "스터디 가입 API", description = "스터디를 가입합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "스터디 가입 성공",
            content = @Content(
                schema = @Schema(
                    implementation = StudyJoinResponseDto.class
                )
            )
        )
    })
    @PostMapping(value = "")
    public ResponseEntity<?> postJoinStudy(@RequestBody StudyJoinRequestDto dto) {
        StudyJoin studyJoin = studyJoinService.joinStudy(dto);

        StudyJoinResponseDto responseDto =
            StudyJoinResponseDto.builder()
                .studyJoinCode(studyJoin.getCode())
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
