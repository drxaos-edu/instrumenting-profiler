package com.example.demo.controller;

import com.example.demo.domain.air.*;
import com.example.demo.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Autowired
    AuthService authService;

    @Autowired
    AirportsRepository airportsRepository;
    @Autowired
    AircraftsRepository aircraftsRepository;
    @Autowired
    BoardingPassesRepository boardingPassesRepository;
    @Autowired
    BookingsRepository bookingsRepository;
    @Autowired
    FlightsRepository flightsRepository;
    @Autowired
    SeatsRepository seatsRepository;
    @Autowired
    TicketFlightsRepository ticketFlightsRepository;
    @Autowired
    TicketsRepository ticketsRepository;

    @RequestMapping(value = {"/", "/index"})
    public String index() {
        return authService.isAuthorised() ? "user/home" : "index";
    }

}
