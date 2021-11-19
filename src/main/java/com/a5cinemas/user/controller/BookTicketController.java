package com.a5cinemas.user.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.a5cinemas.user.dto.MovieCreationDto;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.repo.MovieRepository;

@Controller
public class BookTicketController {

    
    @ModelAttribute("movie")
    public MovieCreationDto movieCreationDto() {
        return new MovieCreationDto();
    }
    
    @Autowired
   	private MovieRepository movieRepository;
    
//    @RequestMapping("/select-time/{movie}")
//    public String selectTime(@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto) {
//        return "select-time";
//    }
    
    @GetMapping("/select-seats")
    public String selectTime(Model model) {
         
        return "select-seats";
    }
    
    @RequestMapping(path = {"/","/select-time"})
    public String selectTime(Model model,
    		@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto,
        BindingResult result, String title) {
        Movie movie = movieRepository.findByTitle(title);
        model.addAttribute("movie", movie);
        return "select-time";
    }
    
    
   
}