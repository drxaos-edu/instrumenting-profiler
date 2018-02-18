package com.example.demo.domain.air;

import javax.persistence.*;

@Entity
@Table(name = "boarding_passes", schema = "bookings", catalog = "demo")
@IdClass(TicketFlightsEntityPK.class)
public class BoardingPassesEntity {
    private String ticketNo;
    private int flightId;
    private int boardingNo;
    private String seatNo;
    private TicketFlightsEntity ticketFlights;

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
    @Column(name = "boarding_no")
    public int getBoardingNo() {
        return boardingNo;
    }

    public void setBoardingNo(int boardingNo) {
        this.boardingNo = boardingNo;
    }

    @Basic
    @Column(name = "seat_no")
    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BoardingPassesEntity that = (BoardingPassesEntity) o;

        if (flightId != that.flightId) return false;
        if (boardingNo != that.boardingNo) return false;
        if (ticketNo != null ? !ticketNo.equals(that.ticketNo) : that.ticketNo != null) return false;
        if (seatNo != null ? !seatNo.equals(that.seatNo) : that.seatNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ticketNo != null ? ticketNo.hashCode() : 0;
        result = 31 * result + flightId;
        result = 31 * result + boardingNo;
        result = 31 * result + (seatNo != null ? seatNo.hashCode() : 0);
        return result;
    }

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "ticket_no", referencedColumnName = "ticket_no", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "flight_id", referencedColumnName = "flight_id", nullable = false, insertable = false, updatable = false)
    })
    public TicketFlightsEntity getTicketFlights() {
        return ticketFlights;
    }

    public void setTicketFlights(TicketFlightsEntity ticketFlights) {
        this.ticketFlights = ticketFlights;
    }
}
