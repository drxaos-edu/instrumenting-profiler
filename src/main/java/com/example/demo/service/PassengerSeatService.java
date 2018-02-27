package com.example.demo.service;

import com.example.demo.data.PassengerSeat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PassengerSeatService {

    @Autowired
    TicketService ticketService;

    @Autowired
    FlightService flightService;

    @Autowired
    ImageService imageService;

    @Autowired
    AirportService airportService;

    public PassengerSeat fill(PassengerSeat ps) {

        ps.setSurnameOptions(ticketService.listAllSurnames());

        if (ps.getSurname() == null) {
            return ps;
        }

        ps.setNameOptions(ticketService.listNamesBySurname(ps.getSurname()));

        if (ps.getNameOptions().size() == 1) {
            ps.setName(ps.getNameOptions().get(0));
        }

        if (ps.getName() == null) {
            return ps;
        }

        ps.setImageData(imageService.searchImage(ps.getName() + " " + ps.getSurname()));

        ps.setIdOptions(ticketService.listIdsByPassengerName(ps.getName() + " " + ps.getSurname()));

        if (ps.getIdOptions().size() == 1) {
            ps.setId(ps.getIdOptions().get(0));
        }

        if (ps.getId() == null) {
            return ps;
        }

        ps.setAirportOptions(airportService.getAirportsByPassenger(ps.getName() + " " + ps.getSurname(), ps.getId()));

        if (ps.getAirportOptions().size() == 1) {
            ps.setAirport(ps.getAirportOptions().get(0));
        }

        if (ps.getAirport() == null) {
            return ps;
        }


        return ps;
    }

}
