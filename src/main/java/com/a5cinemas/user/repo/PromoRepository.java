package com.a5cinemas.user.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.Promotion;
import com.a5cinemas.user.model.User;


@Repository
public interface PromoRepository extends JpaRepository < Promotion, Long > {
    public Promotion findByCode(String code);
}