package com.example.demo.domain.air;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "flights", schema = "bookings", catalog = "demo")
@NamedQuery(name="Flight.findAllJoin", query="SELECT c FROM FlightsEntity c join fetch c.ticketFlightsByFlightId")
public class FlightsEntity {
    private int flightId;
    private String flightNo;
    private Timestamp scheduledDeparture;
    private Timestamp scheduledArrival;
    private String status;
    private Timestamp actualDeparture;
    private Timestamp actualArrival;
    private Airport airportsByDepartureAirport;
    private Airport airportsByArrivalAirport;
    private Aircraft aircraftsByAircraftCode;
    private Collection<TicketFlight> ticketFlightsByFlightId;

    @Id
    @Column(name = "flight_id")
    public int getFlightId() {
        return flightId;
    }

    public void setFlightId(int flightId) {
        this.flightId = flightId;
    }

    @Basic
    @Column(name = "flight_no")
    public String getFlightNo() {
        return flightNo;
    }

    public void setFlightNo(String flightNo) {
        this.flightNo = flightNo;
    }

    @Basic
    @Column(name = "scheduled_departure")
    public Timestamp getScheduledDeparture() {
        return scheduledDeparture;
    }

    public void setScheduledDeparture(Timestamp scheduledDeparture) {
        this.scheduledDeparture = scheduledDeparture;
    }

    @Basic
    @Column(name = "scheduled_arrival")
    public Timestamp getScheduledArrival() {
        return scheduledArrival;
    }

    public void setScheduledArrival(Timestamp scheduledArrival) {
        this.scheduledArrival = scheduledArrival;
    }

    @Basic
    @Column(name = "status")
    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Basic
    @Column(name = "actual_departure")
    public Timestamp getActualDeparture() {
        return actualDeparture;
    }

    public void setActualDeparture(Timestamp actualDeparture) {
        this.actualDeparture = actualDeparture;
    }

    @Basic
    @Column(name = "actual_arrival")
    public Timestamp getActualArrival() {
        return actualArrival;
    }

    public void setActualArrival(Timestamp actualArrival) {
        this.actualArrival = actualArrival;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FlightsEntity that = (FlightsEntity) o;

        if (flightId != that.flightId) return false;
        if (flightNo != null ? !flightNo.equals(that.flightNo) : that.flightNo != null) return false;
        if (scheduledDeparture != null ? !scheduledDeparture.equals(that.scheduledDeparture) : that.scheduledDeparture != null)
            return false;
        if (scheduledArrival != null ? !scheduledArrival.equals(that.scheduledArrival) : that.scheduledArrival != null)
            return false;
        if (status != null ? !status.equals(that.status) : that.status != null) return false;
        if (actualDeparture != null ? !actualDeparture.equals(that.actualDeparture) : that.actualDeparture != null)
            return false;
        if (actualArrival != null ? !actualArrival.equals(that.actualArrival) : that.actualArrival != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = flightId;
        result = 31 * result + (flightNo != null ? flightNo.hashCode() : 0);
        result = 31 * result + (scheduledDeparture != null ? scheduledDeparture.hashCode() : 0);
        result = 31 * result + (scheduledArrival != null ? scheduledArrival.hashCode() : 0);
        result = 31 * result + (status != null ? status.hashCode() : 0);
        result = 31 * result + (actualDeparture != null ? actualDeparture.hashCode() : 0);
        result = 31 * result + (actualArrival != null ? actualArrival.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "departure_airport", referencedColumnName = "airport_code", nullable = false)
    public Airport getAirportsByDepartureAirport() {
        return airportsByDepartureAirport;
    }

    public void setAirportsByDepartureAirport(Airport airportsByDepartureAirport) {
        this.airportsByDepartureAirport = airportsByDepartureAirport;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "arrival_airport", referencedColumnName = "airport_code", nullable = false)
    public Airport getAirportsByArrivalAirport() {
        return airportsByArrivalAirport;
    }

    public void setAirportsByArrivalAirport(Airport airportsByArrivalAirport) {
        this.airportsByArrivalAirport = airportsByArrivalAirport;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_code", referencedColumnName = "aircraft_code", nullable = false, insertable = false, updatable = false)
    public Aircraft getAircraftsByAircraftCode() {
        return aircraftsByAircraftCode;
    }

    public void setAircraftsByAircraftCode(Aircraft aircraftsByAircraftCode) {
        this.aircraftsByAircraftCode = aircraftsByAircraftCode;
    }

    @OneToMany(mappedBy = "flightsByFlightId", fetch = FetchType.LAZY)
    public Collection<TicketFlight> getTicketFlightsByFlightId() {
        return ticketFlightsByFlightId;
    }

    public void setTicketFlightsByFlightId(Collection<TicketFlight> ticketFlightsByFlightId) {
        this.ticketFlightsByFlightId = ticketFlightsByFlightId;
    }
}
