package com.example.demo.data;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class PassengerSeat {

    String surname;
    List<String> surnameOptions;

    boolean nameVisible;
    String name;
    List<String> nameOptions;

    boolean imageDataVisible;
    String imageData;

    boolean idVisible;
    String id;
    List<String> idOptions;

    boolean airportVisible;
    String airport;
    List<String> airportOptions;
    Map<String, String> airportInfo;

    boolean departureVisible;
    String departure;
    List<String> departureOptions;


    boolean flightInfoVisible;
    String flightInfo;
    List<String> passengersInfo;
}
