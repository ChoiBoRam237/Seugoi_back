package com.example.seugoi_back.Study.dto.response;

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
    private List<String> imageList;

    @Schema(name = "isAdmin", example = "true")
    private Boolean isAdmin;

    @Schema(name = "createdAt", example = "2026-06-25 14:43:39.905718")
    private LocalDateTime createdAt;
}
