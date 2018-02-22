package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;

@Data
@EqualsAndHashCode(of = {"ticketNo", "flightId"}, doNotUseGetters = true)
@ToString(of = {"ticketNo", "flightId", "fareConditions", "amount"})
@Entity
@Table(name = "ticket_flights", schema = "bookings", catalog = "demo")
@IdClass(TicketFlightPK.class)
public class TicketFlight {

    @Id
    @Column(name = "ticket_no")
    private String ticketNo;

    @Id
    @Column(name = "flight_id")
    private int flightId;

    @Basic
    @Column(name = "fare_conditions")
    private String fareConditions;

    @Basic
    @Column(name = "amount")
    private BigDecimal amount;

    @OneToOne(mappedBy = "ticketFlights", fetch = FetchType.LAZY, optional = false)
    private BoardingPass boardingPasses;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no", nullable = false, insertable = false, updatable = false)
    private Ticket ticketsByTicketNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", referencedColumnName = "flight_id", nullable = false, insertable = false, updatable = false)
    private Flight flightsByFlightId;
}
