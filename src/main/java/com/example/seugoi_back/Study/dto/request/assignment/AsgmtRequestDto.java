package com.example.seugoi_back.Study.dto.request.assignment;

import com.example.seugoi_back.Util.TextUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsgmtRequestDto {

    @Schema(description = "과제 제목", example = "오늘의 과제입니다.")
    String title;

    @Schema(description = "과제 내용", example = "문제를 풀어주세요")
    String content;

    @Schema(description = "링크 이름", example = "참고 자료")
    String linkName;

    @Schema(description = "링크 URL", example = "aaa.com")
    String linkUrl;

    @Schema(description = "이미지 파일")
    List<MultipartFile> imageList;
}
