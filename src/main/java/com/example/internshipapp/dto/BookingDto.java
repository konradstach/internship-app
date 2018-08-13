package com.example.internshipapp.dto;

import org.springframework.data.annotation.Id;
import com.example.internshipapp.validation.ValidDate;

import javax.validation.constraints.NotNull;

public class BookingDto {

    @Id
    private String id;

    @NotNull
    @ValidDate
    private String startOfBooking;

    @NotNull
    @ValidDate
    private String endOfBooking;

    public BookingDto() {}

    public BookingDto(@NotNull String startOfBooking, @NotNull String endOfBooking) {
        this.startOfBooking = startOfBooking;
        this.endOfBooking = endOfBooking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartOfBooking() {
        return startOfBooking;
    }

    public void setStartOfBooking(String startOfBooking) {
        this.startOfBooking = startOfBooking;
    }

    public String getEndOfBooking() {
        return endOfBooking;
    }

    public void setEndOfBooking(String endOfBooking) {
        this.endOfBooking = endOfBooking;
    }
}
