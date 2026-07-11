package com.example.seugoi_back.Study.dto.response.assignment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AsgmtCmtListResponseDto {

    @Schema(name = "submitted", example = "true")
    private boolean submitted; // 내가 과제 제출을 했는지 안했는지

    @Schema(name = "comments", example = "[]")
    private List<AsgmtCmtResponseDto> comments;
}
