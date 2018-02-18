package com.example.demo.domain.air;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class TicketFlightsEntityPK implements Serializable {
    private String ticketNo;
    private int flightId;

    @Column(name = "ticket_no")
    @Id
    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    @Column(name = "flight_id")
    @Id
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketFlightsEntityPK that = (TicketFlightsEntityPK) o;

        if (flightId != that.flightId) return false;
        if (ticketNo != null ? !ticketNo.equals(that.ticketNo) : that.ticketNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ticketNo != null ? ticketNo.hashCode() : 0;
        result = 31 * result + flightId;
        return result;
    }
}
