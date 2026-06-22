package com.example.seugoi_back.Study.dto.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyJoinRequestDto {

    @NotNull(message = "유저 코드는 필수입니다.")
    @Schema(description = "유저 코드", example = "1")
    Long userCode;

    @NotNull(message = "스터디 코드는 필수입니다.")
    @Schema(description = "스터디 코드", example = "1")
    Long studyCode;
}
