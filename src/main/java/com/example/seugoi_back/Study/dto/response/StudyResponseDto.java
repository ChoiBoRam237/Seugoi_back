package com.example.seugoi_back.Study.dto.response;

import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Study.enums.StudyStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "studyName", example = "스프링")
    private String studyName; // 스터디 이름

    @Schema(name = "categories", example = "['카', '테', '고', '리']")
    private List<String> categories; // 카테고리

    @Schema(name = "dDay", example = "30")
    private Long dDay; // 디데이

    @Schema(name = "progress", example = "50")
    private Integer progress; // 현재 과제 진행상황

    @Schema(name = "bgImg", example = "{}")
    private CommonImgResponseDto bgImg; // 스터디 배경 이미지

    @Schema(name = "isAdmin", example = "true")
    private Boolean isAdmin; // 관리자인지 아닌지

    @Schema(name = "isBookmark", example = "true")
    private Boolean isBookmark; // 북마크 여부

    @Schema(name = "status", example = "STUDYING")
    private StudyStatus status; // 스터디 상태
}
