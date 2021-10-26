package com.a5cinemas.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a5cinemas.user.dto.UserProfileDto;
import com.a5cinemas.user.dto.UserRegistrationDto;
import com.a5cinemas.user.exception.CustomerNotFoundException;
import com.a5cinemas.user.model.Role;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.repo.UserRepository;

import antlr.StringUtils;
import net.bytebuddy.utility.RandomString;


@Service
@Transactional
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User save(UserRegistrationDto registration, String siteURL) throws UnsupportedEncodingException, MessagingException {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setAddress(registration.getAddress());
        user.setCard(registration.getAddress());
        user.setRecievePromotion(registration.getRecievePromotion());
        user.setRoles(Arrays.asList(new Role("USER")));
        String randomCode = RandomString.make(64);
        user.setVerificationCode(randomCode);
        user.setEnabled(Boolean.FALSE);
        User savedUser= userRepository.save(user);
        sendVerificationEmail(user, siteURL);
        return savedUser;
    }
    
    public User save(UserProfileDto registration, String siteURL) throws UnsupportedEncodingException, MessagingException {
        User user = new User();
        user.setFirstName(registration.getFirstName());
        user.setLastName(registration.getLastName());
        user.setEmail(registration.getEmail());
        user.setPassword(passwordEncoder.encode(registration.getPassword()));
        user.setRoles(Arrays.asList(new Role("USER")));
        //String randomCode = RandomString.make(64);
       // user.setVerificationCode(randomCode);
       // user.setEnabled(Boolean.FALSE);
        User savedUser= userRepository.save(user);
       // sendVerificationEmail(user, siteURL);
        return savedUser;
    }
    
    public void save(User user) {
    	userRepository.save(user);
    }
    
    public User get(long id) {
        return userRepository.findById(id).get();
    }
    
    private void sendVerificationEmail(User user, String siteURL)
            throws MessagingException, UnsupportedEncodingException {
        String toAddress = user.getEmail();
        String fromAddress = "support@a5cinemas.com";
        String senderName = "A5 Cinemas Support";
        String subject = "Please verify your registration";
        String content = "Hello [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">Verification Link</a></h3>"
                + "Thanks and Regards,<br>"
                + "A5 Cimeas"
                + "Ph: (706)-714-XXXX"
                + "Add.: Lakeside Dr, Athens-30605";
         
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);
         
        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);
         
        content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
        String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();
         
        content = content.replace("[[URL]]", verifyURL);
         
        helper.setText(content, true);
         
        mailSender.send(message);
         
    }
    
    public Boolean verify(String verificationCode) {
        User user = userRepository.findByVerificationCode(verificationCode);
         
        if (user == null || user.isEnabled()) {
            return Boolean.FALSE;
        } else {
            user.setVerificationCode(null);
            user.setEnabled(true);
            userRepository.save(user);
             
            return Boolean.TRUE;
        }
    }  
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user == null) {
            throw new UsernameNotFoundException("Invalid username or password.");
        }
        return new org.springframework.security.core.userdetails.User(user.getEmail(),
            user.getPassword(),
            mapRolesToAuthorities(user.getRoles()));
    }

    private Collection < ? extends GrantedAuthority > mapRolesToAuthorities(Collection < Role > roles) {
        return roles.stream()
            .map(role -> new SimpleGrantedAuthority(role.getName()))
            .collect(Collectors.toList());
    }
    
    public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
        User user = userRepository.findByEmail(email);
        if (user != null) {
        	user.setResetPasswordToken(token);
        	userRepository.save(user);
        } else {
            throw new CustomerNotFoundException("Could not find any customer with the email " + email);
        }
    }
     
    public User getByResetPasswordToken(String token) {
        return userRepository.findByResetPasswordToken(token);
    }
     
    public void updatePassword(User user, String newPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String encodedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedPassword);
         
        user.setResetPasswordToken(null);
        userRepository.save(user);
    }

    
}