package com.example.seugoi_back.Study.dto.request.notice;

import com.example.seugoi_back.Util.TextUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class NoticeRequestDto {

    @Schema(description = "공지 제목", example = "공지 제목")
    String title;

    @Schema(description = "공지 내용", example = "공지 내용")
    String content;
}
