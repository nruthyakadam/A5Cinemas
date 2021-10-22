package com.a5cinemas.user.service;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.a5cinemas.user.dto.UserRegistrationDto;
import com.a5cinemas.user.model.User;


public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration);
}
