package com.example.seugoi_back.Study.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Value
@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyRequestDto {

    @NotNull(message = "유저 아이디는 필수입니다.")
    @Schema(description = "유저 아이디", example = "1")
    Long userId;

    @NotNull(message = "스터디 이름은 필수입니다.")
    @Schema(description = "스터디 이름", example = "React 공부방")
    String studyName;

    @NotNull(message = "카테고리는 필수입니다.")
    @Schema(description = "카테고리(배열)", example = "[\"카\", \"테\", \"고\", \"리\"]")
    List<String> categories;

    @NotNull(message = "인원수는 필수입니다. 인원 제한 없음일 경우 '-'로 보내시오.")
    @Schema(description = "인원수", example = "10")
    String peopleCount;

    @NotNull(message = "종료 기간은 필수입니다.")
    @Schema(description = "스터드 종료기간", example = "2026.01.01")
    Date endPeriod;

    @Schema(description = "간단 요약 내용", example = "이 스터디는 React 공부방입니다.")
    String summary;

    @Schema(description = "스터디 소개글", example = "[\"소\", \"개\", \"글\"]")
    List<String> introduction;

    @Schema(description = "스터디 설명글", example = "이 스터디 방을 통해 실력 향상 UP! 경험 UP!")
    String description;

    @Schema(description = "추천 유형", example = "[\"추천\", \"유형\"]")
    List<String> recommend;

    @NotNull(message = "배경 이미지는 필수입니다.")
    @Schema(description = "배경 이미지 파일")
    MultipartFile bgImageUrl;
}
