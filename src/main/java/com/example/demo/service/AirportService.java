package com.example.demo.service;

import com.example.demo.domain.air.Airport;
import com.example.demo.domain.air.AirportsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AirportService {

    @Autowired
    AirportsRepository airportsRepository;

    @Autowired
    FlightService flightService;

    @Transactional
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

    @Transactional
    public Map<String, String> getAirportInfos(List<String> airportCodes) {
        Map<String, String> infos = new HashMap<>();
        for (String code : airportCodes) {
            infos.put(code, getAirportInfo(code));
        }
        return infos;
    }

    public String getAirportInfo(String airportCode) {
        Airport airport = airportsRepository.findByAirportCode(airportCode);

        return airportCode + " (" + (airport.getAirportName().equals(airport.getCity()) ? "" : airport.getAirportName() + "/") + airport.getCity() + ")";
    }

}
