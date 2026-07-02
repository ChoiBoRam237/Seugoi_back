package com.example.seugoi_back.Study.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class CommonStudyResponseDto {
    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "userCode", example = "1")
    private Long userCode;

    @Schema(name = "studyCode", example = "1")
    private Long studyCode;
}
