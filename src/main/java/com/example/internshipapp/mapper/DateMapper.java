package com.example.internshipapp.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateMapper {

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm");

    public static String localDateTimeToString(LocalDateTime zonedDateTime) {
        return zonedDateTime.format(formatter);
    }

    public static LocalDateTime stringToLocalDateTime(String string) {
        return LocalDateTime.parse(string, formatter);
    }
}
