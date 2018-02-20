package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = {"ticketNo", "flightId"}, doNotUseGetters = true)
@ToString(of = {"ticketNo", "flightId", "boardingNo", "seatNo"})
@Entity
@Table(name = "boarding_passes", schema = "bookings", catalog = "demo")
@IdClass(TicketFlightsEntityPK.class)
public class BoardingPass {

    @Id
    @Column(name = "ticket_no")
    private String ticketNo;

    @Id
    @Column(name = "flight_id")
    private int flightId;

    @Basic
    @Column(name = "boarding_no")
    private int boardingNo;

    @Basic
    @Column(name = "seat_no")
    private String seatNo;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "flight_id", referencedColumnName = "flight_id", nullable = false, insertable = false, updatable = false)
    })
    private TicketFlight ticketFlights;
}
