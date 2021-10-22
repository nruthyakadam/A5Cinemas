package com.a5cinemas.user.service;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.a5cinemas.user.dto.UserRegistrationDto;
import com.a5cinemas.user.model.User;


public interface UserService extends UserDetailsService {

    User findByEmail(String email);

    User save(UserRegistrationDto registration,  String siteURL) throws UnsupportedEncodingException, MessagingException;

    Boolean verify(String code);
}