package com.example.internshipapp.mapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateMapper {

    public static String localDateTimeToString(LocalDateTime zonedDateTime) {
        return zonedDateTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }

    public static LocalDateTime stringToLocalDateTime(String string) {
        return LocalDateTime.parse(string, DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm"));
    }
}
