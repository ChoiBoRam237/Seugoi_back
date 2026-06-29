package com.example.seugoi_back.Quote.service;

import com.example.seugoi_back.Quote.entity.Quote;
import com.example.seugoi_back.Quote.entity.QuoteDaily;
import com.example.seugoi_back.Quote.repository.QuoteDailyRepository;
import com.example.seugoi_back.Quote.repository.QuoteRepository;
import com.example.seugoi_back.User.service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
public class QuoteService {
    private final QuoteRepository quoteRepository;
    private final QuoteDailyRepository quoteDailyRepository;
    private final UserService userService;

    @Transactional // 오늘의 명언 조회 Service
    public Quote getTodayQuote(Long userCode) {
        LocalDate today = LocalDate.now();
        Optional<QuoteDaily> todayQuote = quoteDailyRepository.findByUser_CodeAndDate(userCode, today);

        if (todayQuote.isPresent()) {
            return todayQuote.get().getQuote();
        }

        // 오늘 처음 조회한 경우
        List<Quote> quotes = quoteRepository.findAll();

        Quote randomQuote = quotes.get(ThreadLocalRandom.current().nextInt(quotes.size()));

        QuoteDaily userDailyQuote = QuoteDaily.builder()
            .user(userService.findUserByCode(userCode))
            .quote(randomQuote)
            .date(today)
            .build();

        quoteDailyRepository.save(userDailyQuote);

        return randomQuote;
    }
}
