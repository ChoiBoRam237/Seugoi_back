package com.example.seugoi_back.Quote.controller;

import com.example.seugoi_back.Common.response.CommonApiResponse;
import com.example.seugoi_back.Quote.entity.Quote;
import com.example.seugoi_back.Quote.service.QuoteService;
import com.example.seugoi_back.User.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v3/api/quote-today")
@Tag(name = "Quote", description = "명언 관련 API")
public class QuoteController {
    private final QuoteService quoteService;

    @Operation(summary = "오늘의 명언 조회 API", description = "하루에 한개씩 랜덤으로 조회합니다.")
    @ApiResponses({
        @ApiResponse(
            responseCode = "true",
            description = "오늘의 명언 조회 성공",
            content = @Content(
                schema = @Schema(
                    implementation = Quote.class
                )
            )
        )
    })
    @GetMapping("")
    public ResponseEntity<?> getTodayQuote(@Parameter(hidden = true) @AuthenticationPrincipal User user) {
        Quote quote = quoteService.getTodayQuote(user.getCode());

        return ResponseEntity.ok(
            CommonApiResponse.builder()
                .success(true)
                .message("오늘의 명언 조회 성공")
                .data(quote)
                .build()
        );
    }
}
