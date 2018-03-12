package com.example.demo.service;

import com.example.demo.domain.air.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketsRepository ticketsRepository;

    @Autowired
    TicketFlightsRepository ticketFlightsRepository;

    @Autowired
    BoardingPassesRepository boardingPassesRepository;

    public List<String> listNamesBySurname(String surname) {
        List<String> names = new ArrayList<>();
        for (String fullName : listPassengersBySurname(surname)) {
            names.add(fullName.split(" ")[0]);
        }
        names.sort(String::compareTo);
        return names;
    }

    public List<String> listPassengersBySurname(String surname) {
        List<Ticket> tickets = ticketsRepository.findByPassengerNameLike("% " + surname.replace("*", "%").toUpperCase());
        return findDistinctNames(tickets);
    }

    private List<String> findDistinctNames(List<Ticket> tickets) {
        List<String> names = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (!names.contains(ticket.getPassengerName())) {
                names.add(ticket.getPassengerName());
            }
        }
        names.sort(String::compareTo);
        return names;
    }

    private List<String> findDistinctIds(List<Ticket> tickets) {
        List<String> ids = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (!ids.contains(ticket.getPassengerId())) {
                ids.add(ticket.getPassengerId());
            }
        }
        ids.sort(String::compareTo);
        return ids;
    }

    public List<String> listIdsByPassengerName(String fullName) {
        List<Ticket> tickets = ticketsRepository.findByPassengerNameLike(fullName.replace("*", "%").toUpperCase());
        return findDistinctIds(tickets);
    }

    public List<String> listTicketsByPassenger(String fullName, String id) {
        List<Ticket> tickets = ticketsRepository.findByPassengerNameLike(fullName.replace("*", "%").toUpperCase());

        List<String> ticketsNo = new ArrayList<>();
        for (Ticket ticket : tickets) {
            if (ticket.getPassengerId().equals(id)) {
                ticketsNo.add(ticket.getTicketNo());
            }
        }
        ticketsNo.sort(String::compareTo);

        return ticketsNo;
    }

    public List<String> listAllSurnames() {
        return ticketsRepository.findAllSurnames();
    }


    public String getTicketByPassengerAndFlight(String fullName, String id, int flight) {
        List<String> tickets = listTicketsByPassenger(fullName, id);
        List<TicketFlight> ticketFlightList = ticketFlightsRepository.findAllByFlightId(flight);
        for (TicketFlight ticketFlight : ticketFlightList) {
            if (tickets.contains(ticketFlight.getTicketNo())) {
                return ticketFlight.getTicketNo();
            }
        }
        return null;
    }

    public String getSeatByPassengerAndFlight(String fullName, String id, int flight) {
        String ticketNo = getTicketByPassengerAndFlight(fullName, id, flight);
        BoardingPass boardingPass = boardingPassesRepository.findByTicketNoAndFlightId(ticketNo, flight);
        return boardingPass != null ? boardingPass.getSeatNo() : null;
    }

    public String getFareConditionsByPassengerAndFlight(String fullName, String id, int flight) {
        String ticketNo = getTicketByPassengerAndFlight(fullName, id, flight);
        TicketFlight ticketFlight = ticketFlightsRepository.findByTicketNoAndFlightId(ticketNo, flight);
        return ticketFlight.getFareConditions();
    }

    public BigDecimal getAmountByPassengerAndFlight(String fullName, String id, int flight) {
        String ticketNo = getTicketByPassengerAndFlight(fullName, id, flight);
        TicketFlight ticketFlight = ticketFlightsRepository.findByTicketNoAndFlightId(ticketNo, flight);
        return ticketFlight.getAmount();
    }

    public String getTicketInfo(String fullName, String id, int flight) {
        String seat = getSeatByPassengerAndFlight(fullName, id, flight);
        return "Seat " + (seat != null ? seat : "N/A") +
                " (" + getFareConditionsByPassengerAndFlight(fullName, id, flight) + ", " + getAmountByPassengerAndFlight(fullName, id, flight) + " RUR)";
    }
}
