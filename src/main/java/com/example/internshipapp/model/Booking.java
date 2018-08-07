package com.example.internshipapp.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;


@Document(collection = "bookings")
public class Booking {

    @Id
    private String id;

    @NotNull
    private LocalDateTime startOfBooking;

    @NotNull
    private LocalDateTime endOfBooking;

    public Booking() {};

    public Booking(@NotNull LocalDateTime startOfBooking, @NotNull LocalDateTime endOfBooking) {
        this.startOfBooking = startOfBooking;
        this.endOfBooking = endOfBooking;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getStartOfBooking() {
        return startOfBooking;
    }

    public void setStartOfBooking(LocalDateTime startOfBooking) {
        this.startOfBooking = startOfBooking;
    }

    public LocalDateTime getEndOfBooking() {
        return endOfBooking;
    }

    public void setEndOfBooking(LocalDateTime endOfBooking) {
        this.endOfBooking = endOfBooking;
    }

    public static Booking clone(Booking sourceBooking){

        Booking booking = new Booking();
        booking.setId(sourceBooking.getId());
        booking.setStartOfBooking(sourceBooking.getStartOfBooking());
        booking.setEndOfBooking(sourceBooking.getEndOfBooking());
        return booking;
    }
}
