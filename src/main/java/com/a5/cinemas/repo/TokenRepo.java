package com.a5.cinemas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a5.cinemas.model.Token;

@Repository
public interface TokenRepo extends JpaRepository<Token, Long> {

    Token findByValue(String value);

}