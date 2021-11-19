package com.a5cinemas.user.controller;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.a5cinemas.user.dto.MovieCreationDto;
import com.a5cinemas.user.dto.ScheduleDto;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.Schedule;
import com.a5cinemas.user.repo.MovieRepository;
import com.a5cinemas.user.repo.ScheduleRepository;
import com.a5cinemas.user.service.MovieService;

@Controller
public class ManageMoviesController {

    @Autowired
    private MovieService movieService;
    
    @Autowired
	private MovieRepository movieRepository;
    
    
    @ModelAttribute("movie")
    public MovieCreationDto movieCreationDto() {
        return new MovieCreationDto();
    }
    
    @ModelAttribute("schedule")
    public ScheduleDto movieScheduleDto() {
        return new ScheduleDto();
    }
    
    @ModelAttribute("date")
    public LocalDateTime scheduleDate() {
        return LocalDateTime.of(LocalDate.MIN, LocalTime.MIN);
    }
    
    @GetMapping("manage_movies")
    public String manageMovies(Model model) {
        return "manage-movies";
    }
    
    @GetMapping("list")
    public String getMovies(Model model) {
        model.addAttribute("movies", movieService.findAll());
        return "index";
    }
    
    @GetMapping("add_new_movie")
    public String showMovieForm(Model model) {
        return "add-new-movie";
    }

    @PostMapping("add_new_movie")
    public String registerUserAccount(@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto,
        BindingResult result) {
    	if(result.hasErrors()) {
            return "add-new-movie";
        }
        movieService.save(movieCreationDto);
        return "redirect:/add_new_movie?success"; //TODO: add success msg in creation.html like registration.html OR return "redirect:/list"
    }
    
   
    
    
    @RequestMapping(path = {"/","/searchByTitle"})
    public String searchByTitle(Model model,
    		@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto,
        BindingResult result, String keyword) {
    	
    	List<Movie> foundMovies = movieRepository.findByKeyword(keyword);
    	List<Movie> movies = movieRepository.findByKeyword(keyword);
    	if(foundMovies.size() == 0) {
        	return  "redirect:/index?success";
        }
    	movieService.fetchUpcomingAndCurrentMovies(model, keyword, foundMovies, movies);
        
        return "index";
    }

	
    
    @RequestMapping(path = {"/","/searchByCategory"})
    public String searchByCategory(Model model,
    		@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto,
        BindingResult result, String keyword) {
        List<Movie> foundMovies = movieRepository.findByCategoryKeyword(movieCreationDto.getGenre());
        List<Movie> movies = movieRepository.findByCategoryKeyword(movieCreationDto.getGenre());
        if(movies.size() == 0) {
        	return  "redirect:/index?failure";
        }
        movieService.fetchUpcomingAndCurrentMovies(model, null==keyword?"":keyword, foundMovies, movies);
        return "index";
    }
    
}