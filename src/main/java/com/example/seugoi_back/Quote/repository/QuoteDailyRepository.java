package com.example.seugoi_back.Quote.repository;

import com.example.seugoi_back.Quote.entity.QuoteDaily;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface QuoteDailyRepository extends JpaRepository<QuoteDaily, Long> {
    Optional<QuoteDaily> findByUser_CodeAndDate(Long userCode, LocalDate today);
}
