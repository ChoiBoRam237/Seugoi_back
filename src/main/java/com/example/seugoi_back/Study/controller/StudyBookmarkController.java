package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyResponseDto;
import com.example.seugoi_back.Study.service.StudyBookmarkService;
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

import java.util.List;
import java.util.Map;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/bookmark")
@Tag(name = "Study Bookmark", description = "스터디 북마크 관련 API")
public class StudyBookmarkController {
    private final StudyBookmarkService studyBookmarkService;

    @Operation(summary = "스터디 북마크 토글 API", description = "스터디를 북마크합니다. (이미 선택한 스터디를 선택할 경우 북마크 해제합니다.)")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "스터디 북마크 토글 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PostMapping("")
    public ResponseEntity<?> postBookmarkStudy(
            @Parameter(hidden = true) @AuthenticationPrincipal User user,
            @RequestParam Long studyCode
    ) {
        Map<String, Object> studyBookmark = studyBookmarkService.bookmarkStudy(user.getCode(), studyCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("스터디 북마크 토글 성공")
                .data(studyBookmark)
                .build()
        );
    }

    @Operation(summary = "내가 북마크한 스터디 목록 조회 API", description = "북마크한 스터디 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "200",
            description = "내가 북마크한 스터디 목록 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = StudyResponseDto.class
                )
            )
        )
    })
    @GetMapping("/{userCode}")
    public ResponseEntity<?> getMyStudy(@PathVariable Long userCode) {
        List<StudyResponseDto> responseDto = studyBookmarkService.findStudyByBookmark(userCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("내가 북마크한 스터디 목록 조회 성공")
                .data(responseDto)
                .build()
        );
    }
}
