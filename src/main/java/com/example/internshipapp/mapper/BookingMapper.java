package com.example.internshipapp.mapper;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.model.Booking;
import org.springframework.stereotype.Component;

@Component
public class BookingMapper implements DtoMapper<Booking, BookingDto> {

    public BookingDto toDto(Booking booking) {

        BookingDto bookingDto = new BookingDto();
        bookingDto.setId(booking.getId());
        bookingDto.setStartOfBooking(DateMapper.localDateTimeToString(booking.getStartOfBooking()));
        bookingDto.setEndOfBooking(DateMapper.localDateTimeToString(booking.getEndOfBooking()));
        return bookingDto;
    }

    public Booking toEntity(BookingDto bookingDto) {

        Booking booking = new Booking();
        booking.setId(bookingDto.getId());
        booking.setStartOfBooking(DateMapper.stringToLocalDateTime(bookingDto.getStartOfBooking()));
        booking.setEndOfBooking(DateMapper.stringToLocalDateTime(bookingDto.getEndOfBooking()));
        return booking;
    }
}
