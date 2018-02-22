package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"aircraftCode"}, doNotUseGetters = true)
@ToString(of = {"aircraftCode", "model", "range"})
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

    @OneToMany(mappedBy = "aircraftsByAircraftCode", fetch = FetchType.LAZY)
    private Collection<Flight> flightsByAircraftCode;

    @OneToMany(mappedBy = "aircraftsByAircraftCode", fetch = FetchType.LAZY)
    private Collection<Seat> seatsByAircraftCode;
}
