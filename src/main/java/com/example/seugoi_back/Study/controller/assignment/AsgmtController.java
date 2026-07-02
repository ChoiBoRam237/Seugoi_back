package com.example.seugoi_back.Study.controller.assignment;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Study.dto.request.assignment.AsgmtRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.StudyBoardResponseDto;
import com.example.seugoi_back.Study.entity.assignment.Asgmt;
import com.example.seugoi_back.Study.service.assignment.AsgmtService;
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

import java.util.List;

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/asgmt")
@Tag(name = "Assignment", description = "과제 관련 API")
public class AsgmtController {
    private final AsgmtService asgmtService;

    @Operation(summary = "스터디 과제 생성 API", description = "스터디 과제를 생성합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "스터디 과제 생성 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PostMapping(value = "/generate", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postGenerateAsgmt(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam Long studyCode,
        @Valid @ModelAttribute AsgmtRequestDto dto
    ) {
        Asgmt asgmt = asgmtService.generateAsgmt(user.getCode(), studyCode, dto);

        CommonStudyResponseDto responseDto =
            CommonStudyResponseDto.builder()
                .code(asgmt.getCode())
                .userCode(asgmt.getUser().getCode())
                .studyCode(asgmt.getStudy().getCode())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("스터디 과제 생성 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "특정 과제 조회 API", description = "특정 과제를 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "특정 과제 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = StudyBoardResponseDto.class
                )
            )
        )
    })
    @GetMapping("/{asgmtCode}")
    public ResponseEntity<?> getByAsgmtCode(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @PathVariable Long asgmtCode
    ) {
        StudyBoardResponseDto responseDto = asgmtService.findByAsgmtCode(user.getCode(), asgmtCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("특정 과제 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "과제 수정 API", description = "과제를 수정합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 수정 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateByAsgmtCode(
        @RequestParam Long asgmtCode,
        @ModelAttribute AsgmtRequestDto dto,
        @RequestParam(required = false) List<Long> removeImgCodeList
    ) {
        CommonStudyResponseDto responseDto = asgmtService.updateAsgmt(asgmtCode, dto, removeImgCodeList);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 수정 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "과제 삭제 API", description = "과제를 삭제합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 삭제 성공"
        )
    })
    @DeleteMapping("/{asgmtCode}")
    public ResponseEntity<?> deleteByAsgmtCode(@PathVariable Long asgmtCode) {
        asgmtService.deleteByAsgmtCode(asgmtCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 삭제 성공")
                .build()
        );
    }
}
