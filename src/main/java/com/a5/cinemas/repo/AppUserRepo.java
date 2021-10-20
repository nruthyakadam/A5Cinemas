package com.a5.cinemas.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.a5.cinemas.model.AppUser;

@Repository
public interface AppUserRepo extends JpaRepository<AppUser, Long> {

    AppUser findByUsername(String username);
    AppUser findByEmail(String email);

}