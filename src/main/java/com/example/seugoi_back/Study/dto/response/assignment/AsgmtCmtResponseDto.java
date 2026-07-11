package com.example.seugoi_back.Study.dto.response.assignment;

import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Login.dto.UserResponseDto;
import com.example.seugoi_back.Study.entity.assignment.AsgmtCmtImg;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
public class AsgmtCmtResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "comment", example = "댓글 내용")
    private String comment;

    @Schema(name = "imgList", example = "['이미지 url']")
    private List<CommonImgResponseDto> imgList;

    @Schema(name = "writerOwner", example = "true")
    private boolean writerOwner; // 작성자인지 아닌지

    @Schema(name = "ownerCheck", example = "true")
    private boolean ownerCheck; // 관리자 체크 여부

    @Schema(name = "createdAt", example = "2026-06-25 14:43:39.905718")
    private LocalDateTime createdAt;

    @Schema(name = "user")
    private UserResponseDto user;
}
