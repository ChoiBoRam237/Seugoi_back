package com.example.seugoi_back.Study.dto.request;

import com.example.seugoi_back.Util.TextUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;

@Value
@Builder
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class StudyNoticeRequestDto {

    @Schema(description = "공지 제목", example = "공지 제목")
    String title;

    @Schema(description = "공지 내용", example = "공지 내용")
    String content;

    @AssertTrue(message = "제목, 내용 중 하나 이상은 존재해야 합니다.")
    @JsonIgnore
    public boolean isNoticeValue() {
        return TextUtil.hasText(title) || TextUtil.hasText(content);
    }
}
