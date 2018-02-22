package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"flightId"}, doNotUseGetters = true)
@ToString(of = {"flightId", "flightNo", "status"})
@Entity
@Table(name = "flights", schema = "bookings", catalog = "demo")
public class Flight {

    @Id
    @Column(name = "flight_id")
    private int flightId;

    @Basic
    @Column(name = "flight_no")
    private String flightNo;

    @Basic
    @Column(name = "scheduled_departure")
    private Timestamp scheduledDeparture;

    @Basic
    @Column(name = "scheduled_arrival")
    private Timestamp scheduledArrival;

    @Basic
    @Column(name = "status")
    private String status;

    @Basic
    @Column(name = "actual_departure")
    private Timestamp actualDeparture;

    @Basic
    @Column(name = "actual_arrival")
    private Timestamp actualArrival;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport", referencedColumnName = "airport_code", nullable = false)
    private Airport airportsByDepartureAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport", referencedColumnName = "airport_code", nullable = false)
    private Airport airportsByArrivalAirport;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_code", referencedColumnName = "aircraft_code", nullable = false, insertable = false, updatable = false)
    private Aircraft aircraftsByAircraftCode;

    @OneToMany(mappedBy = "flightsByFlightId", fetch = FetchType.LAZY)
    private Collection<TicketFlight> ticketFlightsByFlightId;
}
