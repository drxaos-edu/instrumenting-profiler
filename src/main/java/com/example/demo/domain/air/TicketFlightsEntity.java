package com.example.demo.domain.air;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "ticket_flights", schema = "bookings", catalog = "demo")
@IdClass(TicketFlightsEntityPK.class)
public class TicketFlightsEntity {
    private String ticketNo;
    private int flightId;
    private String fareConditions;
    private BigDecimal amount;
    private BoardingPassesEntity boardingPasses;
    private TicketsEntity ticketsByTicketNo;
    private FlightsEntity flightsByFlightId;

    @Id
    @Column(name = "ticket_no")
    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    @Id
    @Column(name = "flight_id")
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    @Basic
    @Column(name = "fare_conditions")
    public String getFareConditions() {
        return fareConditions;
    }

    public void setFareConditions(String fareConditions) {
        this.fareConditions = fareConditions;
    }

    @Basic
    @Column(name = "amount")
    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketFlightsEntity that = (TicketFlightsEntity) o;

        if (flightId != that.flightId) return false;
        if (ticketNo != null ? !ticketNo.equals(that.ticketNo) : that.ticketNo != null) return false;
        if (fareConditions != null ? !fareConditions.equals(that.fareConditions) : that.fareConditions != null)
            return false;
        if (amount != null ? !amount.equals(that.amount) : that.amount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ticketNo != null ? ticketNo.hashCode() : 0;
        result = 31 * result + flightId;
        result = 31 * result + (fareConditions != null ? fareConditions.hashCode() : 0);
        result = 31 * result + (amount != null ? amount.hashCode() : 0);
        return result;
    }

    @OneToOne(mappedBy = "ticketFlights", fetch = FetchType.LAZY,optional = false)
    public BoardingPassesEntity getBoardingPasses() {
        return boardingPasses;
    }

    public void setBoardingPasses(BoardingPassesEntity boardingPasses) {
        this.boardingPasses = boardingPasses;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no", nullable = false, insertable = false, updatable = false)
    public TicketsEntity getTicketsByTicketNo() {
        return ticketsByTicketNo;
    }

    public void setTicketsByTicketNo(TicketsEntity ticketsByTicketNo) {
        this.ticketsByTicketNo = ticketsByTicketNo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "flight_id", referencedColumnName = "flight_id", nullable = false, insertable = false, updatable = false)
    public FlightsEntity getFlightsByFlightId() {
        return flightsByFlightId;
    }

    public void setFlightsByFlightId(FlightsEntity flightsByFlightId) {
        this.flightsByFlightId = flightsByFlightId;
    }
}
