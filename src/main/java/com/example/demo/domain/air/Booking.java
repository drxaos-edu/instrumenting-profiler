package com.example.demo.domain.air;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Data
@EqualsAndHashCode(of = {"bookRef"}, doNotUseGetters = true)
@ToString(of = {"bookRef", "bookDate", "totalAmount"})
@Entity
@Table(name = "bookings", schema = "bookings", catalog = "demo")
public class Booking {
    @Id
    @Column(name = "book_ref")
    private String bookRef;

    @Basic
    @Column(name = "book_date")
    private Timestamp bookDate;

    @Basic
    @Column(name = "total_amount")
    private BigDecimal totalAmount;

    @OneToMany(mappedBy = "bookingsByBookRef", fetch = FetchType.LAZY)
    private Collection<TicketsEntity> ticketsByBookRef;
}
