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
public class ScheduleController {

    @Autowired
    private MovieService movieService;
    
    @Autowired
	private ScheduleRepository scheduleRepository;
    
    @Autowired
	private MovieRepository movieRepository;
    
   
    
    @GetMapping("/{movieTitle}/schedule")
    public String showMovieScheduleForm(Model model, @PathVariable ("movieTitle") String movieTitle) {

        Movie movie = movieService.findByTitle("11");
        model.addAttribute("movie", movie);
        model.addAttribute("schedule", new Schedule());
        model.addAttribute("date", LocalDateTime.of(LocalDate.MIN, LocalTime.MIN));
        return "movie-schedule";
    }

    @PostMapping("/schedule")
    public String addMovieSchedule(
    		@RequestParam("date") LocalDateTime date, BindingResult result) {
    	if(result.hasErrors()) {
            return "movie-schedule";
        }
    	Schedule schedule = new Schedule();
    	schedule.setDate(date);
    	schedule.setMovie(movieRepository.findByTitle("11"));
    	
    	scheduleRepository.save(schedule) ;
        return "redirect:/list";
    }
    
}