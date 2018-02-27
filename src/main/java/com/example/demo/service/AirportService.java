package com.example.demo.service;

import com.example.demo.domain.air.Airport;
import com.example.demo.domain.air.AirportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AirportService {

    @Autowired
    AirportsRepository airportsRepository;

    @Autowired
    FlightService flightService;

    public List<String> getAirportsByPassenger(String fullName, String id) {
        List<Integer> flights = flightService.getFlightsByPassenger(fullName, id);

        List<String> airportCodes = new ArrayList<>();
        for (Integer flight : flights) {
            if (!airportCodes.contains(flightService.getFlightAirportCode(flight))) {
                airportCodes.add(flightService.getFlightAirportCode(flight));
            }
        }
        airportCodes.sort(String::compareTo);

        return airportCodes;
    }

    public String getAirportInfo(String airportCode) {
        Airport airport = airportsRepository.findByAirportCode(airportCode);

        return airportCode + " (" + (airport.getAirportName().equals(airport.getCity()) ? "" : airport.getAirportName() + "/") + airport.getCity() + ")";
    }

}
