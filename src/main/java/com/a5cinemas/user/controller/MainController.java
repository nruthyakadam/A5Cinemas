package com.a5cinemas.user.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.a5cinemas.user.dto.UserProfileDto;
import com.a5cinemas.user.dto.UserRegistrationDto;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.service.UserService;

@Controller
public class MainController {

	@Autowired
    private UserService userService;
	
    @GetMapping("/")
    public String root() {
        return "index";
    }

    @GetMapping("/login")
    public String login(Model model) {
        return "login";
    }

    @GetMapping("/user")
    public String userIndex() {
        return "user/index";
    }
    
    @RequestMapping("/edit/{id}")
    public ModelAndView showEditProductPage(@PathVariable(name = "id") Long id) {
        ModelAndView mav = new ModelAndView("edit-profile");
        User user = userService.get(id);
        mav.addObject("user", user);
         
        return mav;
    }
    
    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public String saveProduct(@ModelAttribute("user")  @Valid User userDto) {
    	userDto.setId(26L);
    	userService.save(userDto);
         
        return "redirect:/";
    }

    
    
    @GetMapping("/account")
    public String viewUserAccountForm(
            @AuthenticationPrincipal CinemaUserDetails userDetails,
            Model model, HttpServletRequest request) {
        String userEmail = request.getUserPrincipal().getName();
        User user = userService.findByEmail(userEmail);
         
        model.addAttribute("user", user);
        model.addAttribute("pageTitle", "Account Details");
         
        return "edit-profile";
    }
    
    @PostMapping("/account/update")
    public String saveDetails(@ModelAttribute("user") @Valid UserProfileDto userDto, RedirectAttributes redirectAttributes,
    		@AuthenticationPrincipal CinemaUserDetails loggedUser, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
    	User user = userService.findByEmail(request.getUserPrincipal().getName());
    	user.setFirstName(userDto.getFirstName());
    	user.setLastName(userDto.getLastName());
    	userService.save(user);
    	//loggedUser.setLastName(user.getFirstName());
    	//loggedUser.setLastName(user.getLastName());
    	redirectAttributes.addFlashAttribute("message", "Your profile has been updated.");
    	return "redirect:/account";
    }
}