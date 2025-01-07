package com.another.ticketmessageservice.component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MapingDate {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");

    public static List<LocalDateTime> getLocalDateFromString(String start, String end) {
        List<LocalDateTime> list = new ArrayList<>();

        // Парсим строки в LocalDate
        LocalDate startDate = LocalDate.parse(start, dateFormatter);
        LocalDate endDate = LocalDate.parse(end, dateFormatter);

        // Преобразуем LocalDate в LocalDateTime, устанавливая время на начало и конец дня
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.atTime(LocalTime.MAX); // или atStartOfDay().plusDays(1).minusNanos(1)

        list.add(startDateTime);
        list.add(endDateTime);

        return list;
    }
}
