package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"ticketNo", "flightId"}, doNotUseGetters = true)
@ToString(of = {"ticketNo", "flightId"})
public class TicketFlightPK implements Serializable {

    @Id
    @Column(name = "ticket_no")
    private String ticketNo;

    @Id
    @Column(name = "flight_id")
    private int flightId;
}
