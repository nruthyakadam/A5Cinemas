package com.a5cinemas.user.repo;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.a5cinemas.user.model.User;


@Repository
public interface UserRepository extends JpaRepository < User, Long > {
	@Query("SELECT u FROM User u WHERE u.email = ?1")
    User findByEmail(String email);
    public User findByResetPasswordToken(String token);
    public User findByVerificationCode(String code);
}