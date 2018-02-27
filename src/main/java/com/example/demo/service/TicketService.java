package com.example.demo.service;

import com.example.demo.domain.air.Ticket;
import com.example.demo.domain.air.TicketsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketsRepository ticketsRepository;

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
        List<Ticket> tickets = ticketsRepository.findByPassengerNameLike("% " + fullName.replace("*", "%").toUpperCase());
        return findDistinctIds(tickets);
    }

    public List<String> listTicketsByPassenger(String fullName, String id) {
        List<Ticket> tickets = ticketsRepository.findByPassengerNameLike("% " + fullName.replace("*", "%").toUpperCase());

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

}
