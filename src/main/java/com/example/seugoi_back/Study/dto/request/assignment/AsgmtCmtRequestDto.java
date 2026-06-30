package com.example.seugoi_back.Study.dto.request.assignment;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class AsgmtCmtRequestDto {

    @NotNull(message = "댓글 내용은 필수입니다.")
    @Schema(description = "댓글 내용", example = "잠시 속도를 늦춰도 우리, 다시 만날거야")
    String comment;

    @Schema(description = "이미지 리스트 (최대 3장)")
    List<MultipartFile> imageList;
}
