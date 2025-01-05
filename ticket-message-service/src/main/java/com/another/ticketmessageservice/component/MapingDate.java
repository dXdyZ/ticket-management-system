package com.another.ticketmessageservice.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class MapingDate {
    private static DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");

    public static List<LocalDateTime> getLocalDateFromString(String start, String end) {
        return new ArrayList<>() {
            {
                add(LocalDateTime.parse(start, dateFormatter));
                add(LocalDateTime.parse(end, dateFormatter));
            }
        };
    }
}
