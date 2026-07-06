package com.example.seugoi_back.Study.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyRequestDto {

    @NotNull(message = "스터디 이름은 필수입니다.")
    @Schema(description = "스터디 이름", example = "React 공부방")
    String studyName;

    @Schema(description = "카테고리(배열)", example = "[\"카\", \"테\", \"고\", \"리\"]")
    List<String> categories;

    @NotNull(message = "인원수는 필수입니다. 인원 제한 없음일 경우 '-'로 보내시오.")
    @Schema(description = "인원수", example = "10")
    String peopleCount;

    @Schema(description = "스터드 종료기간", example = "2026-01-01")
    LocalDate endPeriod;

    @Schema(description = "스터디 제목", example = "React를 공부하고 싶다고요?")
    String studyTitle;

    @Schema(description = "간단 요약 내용", example = "이 스터디는 React 공부방입니다.")
    String summary;

    @Schema(description = "스터디 소개글", example = "[\"소\", \"개\", \"글\"]")
    List<String> introduction;

    @Schema(description = "스터디 설명글", example = "이 스터디 방을 통해 실력 향상 UP! 경험 UP!")
    String description;

    @Schema(description = "추천 유형", example = "[\"추천\", \"유형\"]")
    List<String> recommend;

    @Schema(description = "배경 이미지 파일")
    MultipartFile imgUrl;
}
