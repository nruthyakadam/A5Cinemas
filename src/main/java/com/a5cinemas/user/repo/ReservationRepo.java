package com.a5cinemas.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a5cinemas.user.model.Reservation;

import java.util.List;

@Repository
public interface ReservationRepo extends JpaRepository <Reservation, Long> {
    List<Reservation> findAllByRepertoireId(Long id);
    Reservation findByReservationId(Long id);
}
