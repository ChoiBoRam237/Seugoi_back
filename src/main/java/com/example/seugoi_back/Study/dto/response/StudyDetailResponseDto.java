package com.example.seugoi_back.Study.dto.response;

import com.example.seugoi_back.User.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class StudyDetailResponseDto {

    @Schema(name = "code", example = "1")
    private Long code; // 코드

    @Schema(name = "studyName", example = "스프링")
    private String studyName; // 스터디 이름

    @Schema(name = "categories", example = "['안녕', '하세요']")
    private List<String> categories; // 카테고리

    @Schema(name = "peopleCount", example = "100")
    private String peopleCount; // 모집 인원 (제한 없음: -)

    @Schema(name = "joinCount", example = "30")
    private Long joinCount; // 현재 가입한 인원수

    @Schema(name = "dDay", example = "30")
    private Long dDay; // 디데이

    @Schema(name = "studyTitle", example = "스프링을 배워보자")
    private String studyTitle; // 스터디 제목

    @Schema(name = "summary", example = "스터디 간단 요약")
    private String summary; // 간단 요약

    @Schema(name = "introduction", example = "['안녕']")
    private List<String> introduction; // 스터디 소개

    @Schema(name = "description", example = "스터디 설명")
    private String description; // 설명글

    @Schema(name = "recommend", example = "['추천']")
    private List<String> recommend; // 추천글

    @Schema(name = "bgImageUrl", example = "aaa.png")
    private String bgImageUrl; // 스터디 배경 이미지

    @Schema(name = "isJoined", example = "true")
    private Boolean isJoined; // 스터디 가입 여부

    @Schema(name = "isBookmark", example = "true")
    private Boolean isBookmark; // 북마크 여부
}
