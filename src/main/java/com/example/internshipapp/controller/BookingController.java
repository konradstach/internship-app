package com.example.internshipapp.controller;

import com.example.internshipapp.dto.BookingDto;
import com.example.internshipapp.model.User;
import com.example.internshipapp.service.BookingService;
import io.swagger.annotations.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Api(value = "/bookings", description = "Manage bookings")
@RequestMapping("/bookings")
public class BookingController {

    private BookingService bookingService;

    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @ApiOperation(value = "List all bookings", notes = "List all bookings using paging", response = User.class)
    @GetMapping
    public Page<BookingDto> getBookings(
            @PageableDefault Pageable pageRequest) {
        return bookingService.getBookings(pageRequest);
    }

    @GetMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Booking with given id doesn't exist")
    })
    @ApiOperation(value = "Retreive booking with given id")
    public BookingDto getBookingById(
            @ApiParam(value = "Id to lookup for", required = true)
            @PathVariable(name = "id") String id) {
        return bookingService.getBookingById(id);
    }

    @PostMapping
    @ApiOperation(value = "Create new booking")
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createUser(@RequestBody @Valid BookingDto bookingDto) {

        return bookingService.createBooking(bookingDto);
    }

    @PatchMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Booking with given id doesn't exist")
    })
    @ApiOperation(value = "Modify booking data")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto updateBooking(
            @ApiParam(value = "Id to lookup for", required = true)
            @PathVariable(name = "id") String id,
            @Valid @RequestBody BookingDto bookingDto) {

        return bookingService.updateBooking(bookingDto);
    }

    @DeleteMapping
    @ApiResponses({
            @ApiResponse(code = 204, message = "Booking deleted successfully")
    })
    @ApiOperation(value = "Delete all bookings")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAllBookings() {
        bookingService.deleteAllBookings();
    }

    @DeleteMapping("/{id}")
    @ApiResponses({
            @ApiResponse(code = 404, message = "Booking with given id doesn't exist"),
            @ApiResponse(code = 204, message = "Booking deleted successfully")
    })
    @ApiOperation(httpMethod = "DELETE", value = "Delete booking with given id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBookingById(
            @ApiParam(value = "Id to lookup for", required = true)
            @PathVariable(name = "id") String id) {
        bookingService.deleteBooking(id);
    }
}
