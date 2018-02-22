package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;

@Data
@EqualsAndHashCode(of = {"aircraftCode", "seatNo"}, doNotUseGetters = true)
@ToString(of = {"aircraftCode", "seatNo", "fareConditions"})
@Entity
@Table(name = "seats", schema = "bookings", catalog = "demo")
@IdClass(SeatPK.class)
public class Seat {

    @Id
    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Id
    @Column(name = "seat_no")
    private String seatNo;

    @Basic
    @Column(name = "fare_conditions")
    private String fareConditions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_code", referencedColumnName = "aircraft_code", nullable = false, insertable = false, updatable = false)
    private Aircraft aircraftsByAircraftCode;
}
