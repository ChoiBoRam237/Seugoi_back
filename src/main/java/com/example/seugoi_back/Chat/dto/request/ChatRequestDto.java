package com.example.seugoi_back.Chat.dto.request;

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
public class ChatRequestDto {

    @NotNull(message = "채팅 내용은 필수입니다.")
    @Schema(description = "채팅 내용", example = "채팅 내용입니다.")
    String message;

    @Schema(description = "이미지 파일 리스트", example = "[]")
    List<MultipartFile> imgList;
}
