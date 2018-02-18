package com.example.demo.domain.air;

import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Collection;

@Entity
@Table(name = "bookings", schema = "bookings", catalog = "demo")
public class BookingsEntity {
    private String bookRef;
    private Timestamp bookDate;
    private BigDecimal totalAmount;
    private Collection<TicketsEntity> ticketsByBookRef;

    @Id
    @Column(name = "book_ref")
    public String getBookRef() {
        return bookRef;
    }

    public void setBookRef(String bookRef) {
        this.bookRef = bookRef;
    }

    @Basic
    @Column(name = "book_date")
    public Timestamp getBookDate() {
        return bookDate;
    }

    public void setBookDate(Timestamp bookDate) {
        this.bookDate = bookDate;
    }

    @Basic
    @Column(name = "total_amount")
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BookingsEntity that = (BookingsEntity) o;

        if (bookRef != null ? !bookRef.equals(that.bookRef) : that.bookRef != null) return false;
        if (bookDate != null ? !bookDate.equals(that.bookDate) : that.bookDate != null) return false;
        if (totalAmount != null ? !totalAmount.equals(that.totalAmount) : that.totalAmount != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = bookRef != null ? bookRef.hashCode() : 0;
        result = 31 * result + (bookDate != null ? bookDate.hashCode() : 0);
        result = 31 * result + (totalAmount != null ? totalAmount.hashCode() : 0);
        return result;
    }

    @OneToMany(mappedBy = "bookingsByBookRef", fetch = FetchType.LAZY)
    public Collection<TicketsEntity> getTicketsByBookRef() {
        return ticketsByBookRef;
    }

    public void setTicketsByBookRef(Collection<TicketsEntity> ticketsByBookRef) {
        this.ticketsByBookRef = ticketsByBookRef;
    }
}
