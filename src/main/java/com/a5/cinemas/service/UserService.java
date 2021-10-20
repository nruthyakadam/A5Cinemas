package com.a5.cinemas.service;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.a5.cinemas.model.AppUser;
import com.a5.cinemas.model.Token;
import com.a5.cinemas.repo.AppUserRepo;
import com.a5.cinemas.repo.TokenRepo;

import javax.mail.MessagingException;
import java.util.UUID;


@Service
public class UserService {

    private TokenRepo tokenRepo;
    private MailService mailService;
    private AppUserRepo appUserRepo;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(AppUserRepo appUserRepo, PasswordEncoder passwordEncoder, TokenRepo tokenRepo, MailService mailService) {
        this.appUserRepo = appUserRepo;
        this.passwordEncoder = passwordEncoder;
        this.tokenRepo = tokenRepo;
    }

    public void forgotPassword(AppUser appUser) {
        appUser.setRole("ROLE_USER");
        appUserRepo.save(appUser);
        sendToken(appUser);
    }

    private void sendToken(AppUser appUser) {
        String tokenValue = UUID.randomUUID().toString();
        Token token = new Token();
        token.setValue(tokenValue);
        token.setAppUser(appUser);
        tokenRepo.save(token);
        String url = "localhost:8080/token?value=" + tokenValue;

        try {
            mailService.sendMail(appUser.getEmail(), "Forgot password", url, false);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean appUserEmailExists(String email){
        return appUserRepo.findByEmail(email) !=null;
    }

    public boolean appUserUsernameExists(String username){
        return appUserRepo.findByUsername(username) !=null;
    }
}
