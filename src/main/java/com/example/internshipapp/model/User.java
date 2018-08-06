package com.example.internshipapp.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;


@Document(collection = "users")
public class User {

    @Id
    private String id;

    @NotNull
    @Size(min = 3, max = 50, message = "Username must be 3 to 50 characters in length.")
    private String username;

    @JsonIgnore
    private String password;

    @NotNull
    @Size(min = 3, max = 50, message = "First name must be 3 to 50 characters in length.")
    private String firstName;

    @NotNull
    @Size(min = 3, max = 80, message = "Last name must be 3 to 80 characters in length.")
    private String lastName;

    private double toPay;

    private Booking booking;

    private List<Vehicle> vehicleList;

    public User(@NotNull @Size(min = 3, max = 50, message = "Username must be 3 to 50 characters in length.") String username, String password, @NotNull @Size(min = 3, max = 50, message = "First name must be 3 to 50 characters in length.") String firstName, @NotNull @Size(min = 3, max = 80, message = "Last name must be 3 to 80 characters in length.") String lastName, double toPay, Booking booking, List<Vehicle> vehicleList) {
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.toPay = toPay;
        this.booking = booking;
        this.vehicleList = vehicleList;
    }

    public User() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getToPay() {
        return toPay;
    }

    public void setToPay(double toPay) {
        this.toPay = toPay;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public List<Vehicle> getVehicleList() {
        return vehicleList;
    }

    public void setVehicleList(List<Vehicle> vehicleList) {
        this.vehicleList = vehicleList;
    }
}
