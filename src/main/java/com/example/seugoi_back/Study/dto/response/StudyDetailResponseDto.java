package com.example.seugoi_back.Study.dto.response;

import com.example.seugoi_back.User.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyDetailResponseDto {

    @Schema(name = "studyId", example = "1")
    private Long studyId;

    @Schema(name = "studyName", example = "스프링")
    private String studyName;

    @Schema(name = "categories", example = "['안녕', '하세요']")
    private List<String> categories;

    @Schema(name = "peopleCount", example = "100")
    private String peopleCount; // 모집 인원 (제한 없음: -)

    @Schema(name = "joinCount", example = "30")
    private Integer joinCount; // 현재 가입한 인원수

    @Schema(name = "dDay", example = "30")
    private Long dDay;

    @Schema(name = "studyTitle", example = "스프링을 배워보자")
    private String studyTitle;

    @Schema(name = "summary", example = "스터디 간단 요약")
    private String summary;

    @Schema(name = "introduction", example = "['안녕']")
    private List<String> introduction;

    @Schema(name = "description", example = "스터디 설명")
    private String description;

    @Schema(name = "recommend", example = "['추천']")
    private List<String> recommend;

    @Schema(name = "bgImageUrl", example = "aaa.png")
    private String bgImageUrl; // 스터디 배경 이미지
}
