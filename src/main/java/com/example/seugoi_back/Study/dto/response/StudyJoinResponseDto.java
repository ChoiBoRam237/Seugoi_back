package com.example.seugoi_back.Study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyJoinResponseDto {
    @Schema(name = "userCode", example = "1")
    private Long userCode;

    @Schema(name = "studyCode", example = "1")
    private Long studyCode;

    @Schema(name = "studyJoinCode", example = "1")
    private Long studyJoinCode;
}
