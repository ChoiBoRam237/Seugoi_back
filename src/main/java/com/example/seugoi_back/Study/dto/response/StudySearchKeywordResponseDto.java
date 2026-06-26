package com.example.seugoi_back.Study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudySearchKeywordResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "keyword", example = "java")
    private String keyword;
}
