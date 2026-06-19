package com.example.seugoi_back.Study.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class StudyResponseDto {

    @Schema(name = "adminCode", example = "1")
    private Long adminCode; // 관리자 코드

    @Schema(name = "studyId", example = "1")
    private Long studyId;

    @Schema(name = "studyName", example = "스프링")
    private String studyName;

    @Schema(name = "dDay", example = "30")
    private Long dDay;

    @Schema(name = "progress", example = "50")
    private Integer progress; // 현재 과제 진행상황

    @Schema(name = "bgImageUrl", example = "aaa.png")
    private String bgImageUrl; // 스터디 배경 이미지
}
