package com.example.internshipapp.mapper;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

@RunWith(SpringJUnit4ClassRunner.class)
public class DateMapperTests {

    @Test
    public void localDateTimeToString() {

        LocalDateTime localDateTime = LocalDateTime.of(2018, 8, 8, 12, 30);
        String stringDate = DateMapper.localDateTimeToString(localDateTime);
        Assert.assertEquals("08-08-2018 12:30", stringDate);
    }

    @Test
    public void stringToLocalDateTime() {

        String stringDate = "08-08-2018 12:30";
        LocalDateTime date = DateMapper.stringToLocalDateTime(stringDate);
        Assert.assertEquals(LocalDateTime.of(2018, 8, 8, 12, 30), date);
    }

    @Test(expected = DateTimeParseException.class)
    public void invalidStringToLocalDateTime() {

        String stringDate = "38-18-2018 12:30";
        DateMapper.stringToLocalDateTime(stringDate);
    }
}
