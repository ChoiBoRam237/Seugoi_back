package com.example.seugoi_back.Util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    public static Long calculateDDay(String targetDate) {
        if (targetDate == null || targetDate.isBlank()) {
            return null;
        }

        LocalDate date = LocalDate.parse(targetDate);
        return ChronoUnit.DAYS.between(LocalDate.now(), date);
    }
}
