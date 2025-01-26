package com.another.ticket.component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeMapper {

    // Форматтер для красивого вывода даты и времени
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");

    /**
     * Преобразует LocalDateTime в строку в формате "dd.MM.yyyy HH:mm".
     *
     * @param dateTime Объект LocalDateTime.
     * @return Строка в формате "dd.MM.yyyy HH:mm".
     */
    public static String mapToString(LocalDateTime dateTime) {
        if (dateTime == null) {
            return ""; // или можно вернуть null, либо другое значение по умолчанию
        }
        return dateTime.format(FORMATTER);
    }
}