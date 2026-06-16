package com.example.seugoi_back.Study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyCreateResponseDto {

    @Schema(name = "userId", example = "1")
    private Long userId;

    @Schema(name = "id", example = "1")
    private Long id;
}
