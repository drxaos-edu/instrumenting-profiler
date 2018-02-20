package com.example.demo.domain.air;

import com.github.thealchemist.pg_hibernate.types.Point;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;

import javax.persistence.*;
import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"airportCode"}, doNotUseGetters = true)
@ToString(of = {"airportCode", "airportName", "city", "timezone"})
@Entity
@Table(name = "airports", schema = "bookings", catalog = "demo")
@TypeDefs(value = {
        @TypeDef(name = "point", typeClass = com.github.thealchemist.pg_hibernate.PointType.class),
})
public class Airport {

    @Id
    @Column(name = "airport_code")
    private String airportCode;

    @Basic
    private String airportName;

    @Basic
    private String city;

    @Basic
    @Type(type = "point")
    private Point coordinates;

    @Basic
    private String timezone;

    @OneToMany(mappedBy = "airportsByDepartureAirport", fetch = FetchType.LAZY)
    private Collection<FlightsEntity> flightsByAirportCode;

    @OneToMany(mappedBy = "airportsByArrivalAirport", fetch = FetchType.LAZY)
    private Collection<FlightsEntity> flightsByAirportCode_0;
}
