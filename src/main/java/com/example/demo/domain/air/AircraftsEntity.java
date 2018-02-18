package com.example.demo.domain.air;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "aircrafts", schema = "bookings", catalog = "demo")
public class AircraftsEntity {
    private String aircraftCode;
    private String model;
    private int range;
    private String description;
    private Collection<FlightsEntity> flightsByAircraftCode;
    private Collection<SeatsEntity> seatsByAircraftCode;

    @Id
    @Column(name = "aircraft_code")
    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    @Basic
    @Column(name = "model")
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Basic
    @Column(name = "range")
    public int getRange() {
        return range;
    }

    public void setRange(int range) {
        this.range = range;
    }

    public String getDescription() {
        return description;
    }

    @Basic
    @Column(name = "description")
    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AircraftsEntity that = (AircraftsEntity) o;

        if (range != that.range) return false;
        if (aircraftCode != null ? !aircraftCode.equals(that.aircraftCode) : that.aircraftCode != null) return false;
        if (model != null ? !model.equals(that.model) : that.model != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aircraftCode != null ? aircraftCode.hashCode() : 0;
        result = 31 * result + (model != null ? model.hashCode() : 0);
        result = 31 * result + range;
        return result;
    }

    @OneToMany(mappedBy = "aircraftsByAircraftCode", fetch = FetchType.LAZY)
    public Collection<FlightsEntity> getFlightsByAircraftCode() {
        return flightsByAircraftCode;
    }

    public void setFlightsByAircraftCode(Collection<FlightsEntity> flightsByAircraftCode) {
        this.flightsByAircraftCode = flightsByAircraftCode;
    }

    @OneToMany(mappedBy = "aircraftsByAircraftCode", fetch = FetchType.LAZY)
    public Collection<SeatsEntity> getSeatsByAircraftCode() {
        return seatsByAircraftCode;
    }

    public void setSeatsByAircraftCode(Collection<SeatsEntity> seatsByAircraftCode) {
        this.seatsByAircraftCode = seatsByAircraftCode;
    }

}
