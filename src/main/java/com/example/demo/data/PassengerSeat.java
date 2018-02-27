package com.example.demo.data;

import lombok.Data;

import java.util.List;

@Data
public class PassengerSeat {

    String surname;
    List<String> surnameOptions;

    String name;
    List<String> nameOptions;

    String imageData;

    String id;
    List<String> idOptions;

    String airport;
    List<String> airportOptions;

    String departure;
    List<String> departureOptions;


    String flightInfo;
    List<String> passengersInfo;
}
