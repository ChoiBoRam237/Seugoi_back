package com.example.seugoi_back.Study.dto.response;

import com.example.seugoi_back.Common.response.CommonImgResponseDto;
import com.example.seugoi_back.Study.enums.StudyStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class StudyBoardResponseDto {

    @Schema(name = "code", example = "1")
    private Long code;

    @Schema(name = "studyCode", example = "1")
    private Long studyCode;

    @Schema(name = "target", example = "notice")
    private String target; // notice or asgmt

    @Schema(name = "title", example = "제목")
    private String title;

    @Schema(name = "content", example = "내용")
    private String content;

    @Schema(name = "linkName", example = "링크 이름")
    private String linkName;

    @Schema(name = "linkUrl", example = "링크 url")
    private String linkUrl;

    @Schema(name = "imageList", example = "['이미지 url']")
    private List<CommonImgResponseDto> imgList;

    @Schema(name = "owner", example = "true")
    private boolean owner;

    @Schema(name = "submitted", example = "true")
    private boolean submitted; // 과제 제출 여부

    @Schema(name = "notSubmitCount", example = "10")
    private Long notSubmitCount; // 과제 미제출 인원수

    @Schema(name = "studyStatus", example = "STUDYING")
    private StudyStatus studyStatus;

    @Schema(name = "createdAt", example = "2026-06-25 14:43:39.905718")
    private LocalDateTime createdAt;
}
