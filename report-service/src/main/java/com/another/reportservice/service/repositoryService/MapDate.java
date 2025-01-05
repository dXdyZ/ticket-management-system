package com.another.reportservice.service.repositoryService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MapDate {
    private static DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static List<LocalDate> mapDate(String start, String end) {
        validationDate(start);
        validationDate(end);
        return new ArrayList<>() {
            {
                add(LocalDate.parse(start, dateTimeFormatter));
                add(LocalDate.parse(end, dateTimeFormatter));
            }
        };
    }

    public static void validationDate(String date) {
        if (!date.matches("\\d{4}\\.\\d{2}\\.\\d{2}")) {
            throw new IllegalArgumentException("Invalid data format: " + date);
        }
    }
}
