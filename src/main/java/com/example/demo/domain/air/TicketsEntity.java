package com.example.demo.domain.air;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tickets", schema = "bookings", catalog = "demo")
public class TicketsEntity {
    private String ticketNo;
    private String bookRef;
    private String passengerId;
    private String passengerName;
    private Collection<TicketFlight> ticketFlightsByTicketNo;
    private Booking bookingsByBookRef;

    @Id
    @Column(name = "ticket_no")
    public String getTicketNo() {
        return ticketNo;
    }

    public void setTicketNo(String ticketNo) {
        this.ticketNo = ticketNo;
    }

    @Basic
    @Column(name = "book_ref")
    public String getBookRef() {
        return bookRef;
    }

    public void setBookRef(String bookRef) {
        this.bookRef = bookRef;
    }

    @Basic
    @Column(name = "passenger_id")
    public String getPassengerId() {
        return passengerId;
    }

    public void setPassengerId(String passengerId) {
        this.passengerId = passengerId;
    }

    @Basic
    @Column(name = "passenger_name")
    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TicketsEntity that = (TicketsEntity) o;

        if (ticketNo != null ? !ticketNo.equals(that.ticketNo) : that.ticketNo != null) return false;
        if (bookRef != null ? !bookRef.equals(that.bookRef) : that.bookRef != null) return false;
        if (passengerId != null ? !passengerId.equals(that.passengerId) : that.passengerId != null) return false;
        if (passengerName != null ? !passengerName.equals(that.passengerName) : that.passengerName != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = ticketNo != null ? ticketNo.hashCode() : 0;
        result = 31 * result + (bookRef != null ? bookRef.hashCode() : 0);
        result = 31 * result + (passengerId != null ? passengerId.hashCode() : 0);
        result = 31 * result + (passengerName != null ? passengerName.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "ticketsByTicketNo", fetch = FetchType.LAZY)
    public Collection<TicketFlight> getTicketFlightsByTicketNo() {
        return ticketFlightsByTicketNo;
    }

    public void setTicketFlightsByTicketNo(Collection<TicketFlight> ticketFlightsByTicketNo) {
        this.ticketFlightsByTicketNo = ticketFlightsByTicketNo;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_ref", referencedColumnName = "book_ref", nullable = false, insertable = false, updatable = false)
    public Booking getBookingsByBookRef() {
        return bookingsByBookRef;
    }

    public void setBookingsByBookRef(Booking bookingsByBookRef) {
        this.bookingsByBookRef = bookingsByBookRef;
    }
}
