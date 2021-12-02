package com.a5cinemas.user.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a5cinemas.user.model.Repertoire;

import java.util.List;

@Repository
public interface RepertoireRepo extends JpaRepository<Repertoire, Long> {
    List<Repertoire> findByMovieId(Long movieId);
    List<Repertoire> findBySpectacleId(Long spectacleId);
}
