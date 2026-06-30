package com.example.seugoi_back.Study.dto.response.assignment;

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

    @Schema(name = "imageList", example = "['이미지 url']")
    private List<String> imageList;

    @Schema(name = "isWriter", example = "true")
    private Boolean isWriter; // 작성자인지 아닌지

    @Schema(name = "createdAt", example = "2026-06-25 14:43:39.905718")
    private LocalDateTime createdAt;
}
