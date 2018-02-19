package com.example.demo.domain.air;

import org.postgresql.geometric.PGpoint;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "airports", schema = "bookings", catalog = "demo")
public class AirportsEntity {
    private String airportCode;
    private String airportName;
    private String city;
    private PGpoint coordinates;
    private String timezone;
    private Collection<FlightsEntity> flightsByAirportCode;
    private Collection<FlightsEntity> flightsByAirportCode_0;

    @Id
    @Column(name = "airport_code")
    public String getAirportCode() {
        return airportCode;
    }

    public void setAirportCode(String airportCode) {
        this.airportCode = airportCode;
    }

    @Basic
    @Column(name = "airport_name")
    public String getAirportName() {
        return airportName;
    }

    public void setAirportName(String airportName) {
        this.airportName = airportName;
    }

    @Basic
    @Column(name = "city")
    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    @Basic
    @Column(name = "coordinates")
    public PGpoint getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(PGpoint coordinates) {
        this.coordinates = coordinates;
    }

    @Basic
    @Column(name = "timezone")
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AirportsEntity that = (AirportsEntity) o;

        if (Double.compare(that.coordinates.x, coordinates.x) != 0) return false;
        if (Double.compare(that.coordinates.y, coordinates.y) != 0) return false;
        if (airportCode != null ? !airportCode.equals(that.airportCode) : that.airportCode != null) return false;
        if (airportName != null ? !airportName.equals(that.airportName) : that.airportName != null) return false;
        if (city != null ? !city.equals(that.city) : that.city != null) return false;
        if (timezone != null ? !timezone.equals(that.timezone) : that.timezone != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = airportCode != null ? airportCode.hashCode() : 0;
        result = 31 * result + (airportName != null ? airportName.hashCode() : 0);
        result = 31 * result + (city != null ? city.hashCode() : 0);
        temp = Double.doubleToLongBits(coordinates.x);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(coordinates.y);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (timezone != null ? timezone.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "airportsByDepartureAirport", fetch = FetchType.LAZY)
    public Collection<FlightsEntity> getFlightsByAirportCode() {
        return flightsByAirportCode;
    }

    public void setFlightsByAirportCode(Collection<FlightsEntity> flightsByAirportCode) {
        this.flightsByAirportCode = flightsByAirportCode;
    }

    @OneToMany(mappedBy = "airportsByArrivalAirport", fetch = FetchType.LAZY)
    public Collection<FlightsEntity> getFlightsByAirportCode_0() {
        return flightsByAirportCode_0;
    }

    public void setFlightsByAirportCode_0(Collection<FlightsEntity> flightsByAirportCode_0) {
        this.flightsByAirportCode_0 = flightsByAirportCode_0;
    }
}
