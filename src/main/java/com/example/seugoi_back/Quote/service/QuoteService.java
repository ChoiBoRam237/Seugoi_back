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
        List<Quote> quotes = quoteRepository.findAll();
        Optional<QuoteDaily> myQuote = quoteDailyRepository.findByUser_Code(userCode);
        Optional<QuoteDaily> todayQuote = quoteDailyRepository.findByUser_CodeAndDate(userCode, today);
        Quote randomQuote = quotes.get(ThreadLocalRandom.current().nextInt(quotes.size()));

        // 오늘 조회한 적이 있는 경우
        if (todayQuote.isPresent()) {
            return todayQuote.get().getQuote();
        }

        // 오늘 조회한 적은 없지만 예전에 조회한 적이 있는 경우
        if (myQuote.isPresent()) {
            myQuote.get().update(randomQuote, today);
            return randomQuote;
        }

        // 오늘 처음 조회한 경우
        QuoteDaily userDailyQuote = QuoteDaily.builder()
            .user(userService.findByUserCode(userCode))
            .quote(randomQuote)
            .date(today)
            .build();

        quoteDailyRepository.save(userDailyQuote);

        return randomQuote;
    }
}
