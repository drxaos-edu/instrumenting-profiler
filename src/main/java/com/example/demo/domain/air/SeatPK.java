package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

@Data
@EqualsAndHashCode(of = {"aircraftCode", "seatNo"}, doNotUseGetters = true)
@ToString(of = {"aircraftCode", "seatNo"})
public class SeatPK implements Serializable {

    @Id
    @Column(name = "aircraft_code")
    private String aircraftCode;

    @Id
    @Column(name = "seat_no")
    private String seatNo;

}
