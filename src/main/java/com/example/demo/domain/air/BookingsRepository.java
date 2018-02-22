package com.example.demo.domain.air;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface BookingsRepository extends CrudRepository<Booking, String> {

    Booking findByBookRef(String bookRef);

    List<Booking> findAllByBookDateBetween(Date from, Date to);
}
