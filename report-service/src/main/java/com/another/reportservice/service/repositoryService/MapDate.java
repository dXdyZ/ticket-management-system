package com.another.reportservice.service.repositoryService;

import com.another.reportservice.custom_exception.FutureDateException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MapDate {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static List<LocalDateTime> mapDate(String start, String end) throws FutureDateException {
        validationDate(start);
        validationDate(end);
        List<LocalDateTime> date =  new ArrayList<>() {{
            add(LocalDate.parse(start, dateTimeFormatter).atStartOfDay()); // Добавляет время 00:00
            add(LocalDate.parse(end, dateTimeFormatter).atStartOfDay());   // Добавляет время 00:00
        }};
        checkDateForFuture(date.get(0), date.get(1));
        return date;
    }

    public static void checkDateForFuture(LocalDateTime start, LocalDateTime end) throws FutureDateException {
        LocalDate now = LocalDate.now();
        LocalDateTime dateTime = now.atStartOfDay();
        if (start.isAfter(dateTime)) {
            throw new FutureDateException("Date is in the future: " + start);
        }
        if (end.isAfter(dateTime)) {
            throw new FutureDateException("Date is in the future: " + start);
        }
    }

    public static void validationDate(String date) {
        if (!date.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            throw new IllegalArgumentException("Invalid data format: " + date);
        }
    }
}

