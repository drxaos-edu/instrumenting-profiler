package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"ticketNo"}, doNotUseGetters = true)
@ToString(of = {"ticketNo", "bookRef", "passengerId", "passengerName"})
@Entity
@Table(name = "tickets", schema = "bookings", catalog = "demo")
public class Ticket {

    @Id
    @Column(name = "ticket_no")
    private String ticketNo;

    @Basic
    @Column(name = "book_ref")
    private String bookRef;

    @Basic
    @Column(name = "passenger_id")
    private String passengerId;

    @Basic
    @Column(name = "passenger_name")
    private String passengerName;

    @OneToMany(mappedBy = "ticketsByTicketNo", fetch = FetchType.LAZY)
    private Collection<TicketFlight> ticketFlightsByTicketNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_ref", referencedColumnName = "book_ref", nullable = false, insertable = false, updatable = false)
    private Booking bookingsByBookRef;
}
