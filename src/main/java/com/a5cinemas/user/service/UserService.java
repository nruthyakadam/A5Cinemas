package com.a5cinemas.user.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.validation.Valid;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.a5cinemas.user.dto.UserProfileDto;
import com.a5cinemas.user.dto.UserRegistrationDto;
import com.a5cinemas.user.exception.CustomerNotFoundException;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.User;


public interface UserService extends UserDetailsService {

    CinemaUserDetails findByEmail(String email);

    User save(UserRegistrationDto registration,  String siteURL) throws UnsupportedEncodingException, MessagingException;
    
    User save(UserProfileDto profile) throws UnsupportedEncodingException, MessagingException, CustomerNotFoundException;

    Boolean verify(String code);

    public User get(long id);
}
