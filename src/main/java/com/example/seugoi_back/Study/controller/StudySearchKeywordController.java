package com.example.seugoi_back.Study.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.response.StudySearchKeywordResponseDto;
import com.example.seugoi_back.Study.service.StudySearchKeywordService;
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

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/search-keyword")
@Tag(name = "Search Keyword", description = "검색어 관련 API")
public class StudySearchKeywordController {
    private final StudySearchKeywordService studySearchKeywordService;

    @Operation(summary = "최근 검색어 조회 API", description = "최근 검색했던 검색어를 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "최근 검색어 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = StudySearchKeywordResponseDto.class
                )
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<?> getSearchKeyword(@Parameter(hidden = true) @AuthenticationPrincipal User user) {
        List<StudySearchKeywordResponseDto> responseDto =
            studySearchKeywordService.findKeywordByCode(user.getCode());

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("최근 검색어 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "검색어 전체 삭제 API", description = "검색어를 전체 삭제합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "검색어 전체 삭제 성공"
        )
    })
    @DeleteMapping("/all")
    public ResponseEntity<?> deleteAllSearchKeyword(@Parameter(hidden = true) @AuthenticationPrincipal User user) {
        studySearchKeywordService.deleteAllKeyword(user.getCode());

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("검색어 전체 삭제 성공")
                .build()
        );
    }

    @Operation(summary = "검색어 삭제 API", description = "검색어를 삭제합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "검색어 삭제 성공"
        )
    })
    @DeleteMapping("/{keywordCode}")
    public ResponseEntity<?> deleteSearchKeyword(@PathVariable Long keywordCode) {
        studySearchKeywordService.deleteKeyword(keywordCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("검색어 삭제 성공")
                .build()
        );
    }
}
