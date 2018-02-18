package com.example.demo.domain.air;

import javax.persistence.*;

@Entity
@Table(name = "seats", schema = "bookings", catalog = "demo")
@IdClass(SeatsEntityPK.class)
public class SeatsEntity {
    private String aircraftCode;
    private String seatNo;
    private String fareConditions;
    private AircraftsEntity aircraftsByAircraftCode;

    @Id
    @Column(name = "aircraft_code")
    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    @Id
    @Column(name = "seat_no")
    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    @Basic
    @Column(name = "fare_conditions")
    public String getFareConditions() {
        return fareConditions;
    }

    public void setFareConditions(String fareConditions) {
        this.fareConditions = fareConditions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeatsEntity that = (SeatsEntity) o;

        if (aircraftCode != null ? !aircraftCode.equals(that.aircraftCode) : that.aircraftCode != null) return false;
        if (seatNo != null ? !seatNo.equals(that.seatNo) : that.seatNo != null) return false;
        if (fareConditions != null ? !fareConditions.equals(that.fareConditions) : that.fareConditions != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aircraftCode != null ? aircraftCode.hashCode() : 0;
        result = 31 * result + (seatNo != null ? seatNo.hashCode() : 0);
        result = 31 * result + (fareConditions != null ? fareConditions.hashCode() : 0);
        return result;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aircraft_code", referencedColumnName = "aircraft_code", nullable = false, insertable = false, updatable = false)
    public AircraftsEntity getAircraftsByAircraftCode() {
        return aircraftsByAircraftCode;
    }

    public void setAircraftsByAircraftCode(AircraftsEntity aircraftsByAircraftCode) {
        this.aircraftsByAircraftCode = aircraftsByAircraftCode;
    }
}
