package com.a5cinemas.user.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.a5cinemas.user.Utility.Utility;
import com.a5cinemas.user.exception.CustomerNotFoundException;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.service.UserServiceImpl;

import net.bytebuddy.utility.RandomString;

@Controller
public class ForgotPasswordController {

	@Autowired
    private JavaMailSender mailSender;
     
    @Autowired
    private UserServiceImpl userService;
     
    @GetMapping("/forgot_password")
    public String showForgotPasswordForm() {
        return "forgot_password_form";
    }
 
    @PostMapping("/forgot_password")
    public String processForgotPassword(HttpServletRequest request, Model model) {
        String email = request.getParameter("email");
        String token = RandomString.make(30);
         
        try {
        	userService.updateResetPasswordToken(token, email);
            String resetPasswordLink = Utility.getSiteURL(request) + "/reset_password?token=" + token;
            sendEmail(email, resetPasswordLink);
            model.addAttribute("message", "We have sent a reset password link to your email.");
             
        } catch (CustomerNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }
             
        return "forgot_password_form";
    }
     
    public void sendEmail(String recipientEmail, String link)
            throws MessagingException, UnsupportedEncodingException {
        MimeMessage message = mailSender.createMimeMessage();              
        MimeMessageHelper helper = new MimeMessageHelper(message);
         
        helper.setFrom("support@a5cinemas.com", "A5 Cinemas Support");
        helper.setTo(recipientEmail);
         
        String subject = "Link to reset your password";
         
        String content = "<p>Hello,</p>"
                + "<p>You have requested to reset your password.</p>"
                + "<p>Please click the link below to change the same:</p>"
                + "<h3><a href=\"" + link + "\">Change my password</a></h3>"
                + "<br>"
                + "<p>Ignore this email if you have not made the request.</p>"
                + "Thanks and Regards,<br>"
                + "A5 Cimeas<br>"
                + "Ph: (706)-714-XXXX<br>"
                + "Add.: Lakeside Dr, Athens-30605";;
         
        helper.setSubject(subject);
         
        helper.setText(content, true);
        mailSender.send(message);
    }
     
     
    @GetMapping("/reset_password")
    public String showResetPasswordForm(@Param(value = "token") String token, Model model) {
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("token", token);
         
        if (user == null) {
            model.addAttribute("message", "Invalid Token");
            return "message";
        }
         
        return "reset_password_form";
    }
    @PostMapping("/reset_password")
    public String processResetPassword(HttpServletRequest request, Model model) {
        String token = request.getParameter("token");
        String password = request.getParameter("password");
         
        User user = userService.getByResetPasswordToken(token);
        model.addAttribute("title", "Reset your password");
         
        if (user == null) {
            model.addAttribute("message", "Invalid reset password link.");
            
        } else {           
        	userService.updatePassword(user, password);
             
            model.addAttribute("message", "You have successfully changed your password.");
        }
         
        return "reset_password_form";
    }
    
    @GetMapping("/verify")
    public String verifyUser(@Param(value = "code") String code) {
        if (userService.verify(code)) {
            return "verify_success";
        } else {
            return "verify_fail";
        }
    }
}