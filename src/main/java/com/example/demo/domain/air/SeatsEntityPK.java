package com.example.demo.domain.air;

import javax.persistence.Column;
import javax.persistence.Id;
import java.io.Serializable;

public class SeatsEntityPK implements Serializable {
    private String aircraftCode;
    private String seatNo;

    @Column(name = "aircraft_code")
    @Id
    public String getAircraftCode() {
        return aircraftCode;
    }

    public void setAircraftCode(String aircraftCode) {
        this.aircraftCode = aircraftCode;
    }

    @Column(name = "seat_no")
    @Id
    public String getSeatNo() {
        return seatNo;
    }

    public void setSeatNo(String seatNo) {
        this.seatNo = seatNo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SeatsEntityPK that = (SeatsEntityPK) o;

        if (aircraftCode != null ? !aircraftCode.equals(that.aircraftCode) : that.aircraftCode != null) return false;
        if (seatNo != null ? !seatNo.equals(that.seatNo) : that.seatNo != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = aircraftCode != null ? aircraftCode.hashCode() : 0;
        result = 31 * result + (seatNo != null ? seatNo.hashCode() : 0);
        return result;
    }
}
