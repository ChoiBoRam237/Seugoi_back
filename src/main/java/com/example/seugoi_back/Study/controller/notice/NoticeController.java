package com.example.seugoi_back.Study.controller.notice;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.notice.NoticeRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyBoardResponseDto;
import com.example.seugoi_back.Study.entity.notice.Notice;
import com.example.seugoi_back.Study.service.notice.NoticeService;
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
import org.springframework.web.bind.annotation.*;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/notice")
@Tag(name = "Notice", description = "공지 관련 API")
public class NoticeController {
    private final NoticeService noticeService;

    @Operation(summary = "공지 생성 API", description = "공지를 생성합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "공지 생성 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PostMapping("/generate")
    public ResponseEntity<?> postGenerateNotice(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam Long studyCode,
        @Valid @RequestBody NoticeRequestDto dto
    ) {
        Notice notice = noticeService.generateNotice(user.getCode(), studyCode, dto);

        CommonStudyResponseDto responseDto =
            CommonStudyResponseDto.builder()
                .code(notice.getCode())
                .userCode(notice.getUser().getCode())
                .studyCode(notice.getStudy().getCode())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("공지 생성 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "특정 공지 조회 API", description = "특정 공지를 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "특정 공지 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = StudyBoardResponseDto.class
                )
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<?> getNotice(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam Long noticeCode
    ) {
        StudyBoardResponseDto responseDto = noticeService.findByNoticeCode(user.getCode(), noticeCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("특정 공지 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "공지 수정 API", description = "공지를 수정합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "공지 수정 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PatchMapping("/update")
    public ResponseEntity<?> updateByNoticeCode(
        @RequestParam Long noticeCode,
        @RequestBody NoticeRequestDto dto
    ) {
        CommonStudyResponseDto responseDto = noticeService.updateNotice(noticeCode, dto);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("공지 수정 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "공지 삭제 API", description = "공지를 삭제합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "공지 삭제 성공"
        )
    })
    @DeleteMapping("/{noticeCode}")
    public ResponseEntity<?> deleteByNoticeCode(@PathVariable Long noticeCode) {
        noticeService.deleteByNoticeCode(noticeCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("공지 삭제 성공")
                .build()
        );
    }
}
