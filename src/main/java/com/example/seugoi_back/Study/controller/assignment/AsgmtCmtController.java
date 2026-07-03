package com.example.seugoi_back.Study.controller.assignment;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Study.dto.request.assignment.AsgmtCmtRequestDto;
import com.example.seugoi_back.Study.dto.response.CommonCreateResponseDto;
import com.example.seugoi_back.Study.dto.response.CommonStudyResponseDto;
import com.example.seugoi_back.Study.dto.response.assignment.AsgmtCmtListResponseDto;
import com.example.seugoi_back.Study.entity.assignment.AsgmtCmt;
import com.example.seugoi_back.Study.service.assignment.AsgmtCmtImgService;
import com.example.seugoi_back.Study.service.assignment.AsgmtCmtService;
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

@SecurityRequirement(name = "BearerAuth")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/asgmt-cmt")
@Tag(name = "Assignment Comment", description = "과제 댓글 관련 API")
public class AsgmtCmtController {
    private final AsgmtCmtService asgmtCmtService;
    private final AsgmtCmtImgService asgmtCmtImgService;

    @Operation(summary = "과제 댓글 생성 API", description = "과제 댓글을 생성합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 댓글 생성 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonCreateResponseDto.class
                )
            )
        )
    })
    @PostMapping(value =  "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> postAsgmtCmt(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam Long asgmtCode,
        @ModelAttribute AsgmtCmtRequestDto dto
    ) {
        AsgmtCmt asgmtCmt = asgmtCmtService.generateAsgmtCmt(user.getCode(), asgmtCode, dto);

        CommonCreateResponseDto responseDto =
            CommonCreateResponseDto.builder()
                .code(asgmtCmt.getCode())
                .userCode(asgmtCmt.getUser().getCode())
                .build();

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 댓글 생성 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "과제 댓글 조회 API", description = "과제 댓글을 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 댓글 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = AsgmtCmtListResponseDto.class
                )
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<?> getAsgmtCmtAll(
        @Parameter(hidden = true) @AuthenticationPrincipal User user,
        @RequestParam Long asgmtCode
    ) {
        AsgmtCmtListResponseDto responseDto = asgmtCmtService.findByAsgmtCode(user.getCode(), asgmtCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 댓글 조회 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "과제 댓글 수정 API", description = "과제 댓글을 수정합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 댓글 수정 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonStudyResponseDto.class
                )
            )
        )
    })
    @PatchMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateByAsgmtCmtCode(
        @RequestParam Long asgmtCmtCode,
        @ModelAttribute AsgmtCmtRequestDto dto,
        @RequestParam(required = false) List<Long> removeImgCodeList
    ) {
        CommonStudyResponseDto responseDto = asgmtCmtService.updateAsgmtCmt(asgmtCmtCode, dto, removeImgCodeList);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 댓글 수정 성공")
                .data(responseDto)
                .build()
        );
    }

    @Operation(summary = "과제 댓글 삭제 API", description = "댓글을 삭제합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 댓글 삭제 성공"
        )
    })
    @DeleteMapping("/{asgmtCmtCode}")
    public ResponseEntity<?> deleteAsgmtCmt(@PathVariable Long asgmtCmtCode) {
        asgmtCmtService.deleteByAsgmtCmtCode(asgmtCmtCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 댓글 삭제 성공")
                .build()
        );
    }

    @Operation(summary = "과제 댓글 이미지 조회 API", description = "과제 댓글의 이미지 목록을 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 댓글 이미지 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = CommonImgResponseDto.class
                )
            )
        )
    })
    @GetMapping("/imgList")
    public ResponseEntity<?> getAsgmtCmtImgList(@RequestParam Long asgmtCmtCode) {
        List<CommonImgResponseDto> imgList = asgmtCmtImgService.findByAsgmtCmtCode(asgmtCmtCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 댓글 이미지 조회 성공")
                .data(imgList)
                .build()
        );
    }

    @Operation(summary = "과제 댓글 확인 처리 API (관리자용)", description = "댓글을 확인 처리합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "과제 댓글 확인 처리 성공"
        )
    })
    @PostMapping("/submit")
    public ResponseEntity<?> postAsgmtCmtSubmit(@RequestParam Long asgmtCmtCode) {
        asgmtCmtService.submitAsgmtCmt(asgmtCmtCode);

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("과제 댓글 확인 처리 성공")
                .build()
        );
    }
}
