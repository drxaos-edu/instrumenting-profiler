package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"aircraftCode"}, doNotUseGetters = true)
@ToString(of = {"aircraftCode", "model", "range", "description"})
@Entity
@Table(name = "aircrafts", schema = "bookings", catalog = "demo")
public class Aircraft {
    @Id
    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Basic
    private String model;

    @Basic
    private int range;

    @Basic
    private String description;

    @OneToMany(mappedBy = "aircraftsByAircraftCode", fetch = FetchType.LAZY)
    private Collection<FlightsEntity> flightsByAircraftCode;

    @OneToMany(mappedBy = "aircraftsByAircraftCode", fetch = FetchType.LAZY)
    private Collection<SeatsEntity> seatsByAircraftCode;
}
