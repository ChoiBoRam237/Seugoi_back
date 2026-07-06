package com.example.seugoi_back.Util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class DateUtil {
    // 디데이 계산 util
    public static Long calculateDDay(LocalDate targetDate) {
        if (targetDate == null) {
            return null;
        }

        return ChronoUnit.DAYS.between(LocalDate.now(), targetDate);
    }
}
