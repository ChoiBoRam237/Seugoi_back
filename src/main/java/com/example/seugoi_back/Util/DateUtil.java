package com.example.seugoi_back.Util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    // 디데이 계산 util
    public static Long calculateDDay(String targetDate) {
        if (targetDate == null || targetDate.isBlank()) {
            return null;
        }

        LocalDate date = LocalDate.parse(targetDate);
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
}
