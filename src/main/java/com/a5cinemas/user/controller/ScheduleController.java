package com.a5cinemas.user.controller;

import java.sql.Date;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.a5cinemas.user.dto.MovieCreationDto;
import com.a5cinemas.user.dto.ScheduleDto;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.Schedule;
import com.a5cinemas.user.repo.MovieRepository;
import com.a5cinemas.user.repo.ScheduleRepository;
import com.a5cinemas.user.service.MovieService;

@Controller
@RequestMapping({"/schedule" })
public class ScheduleController {

    @Autowired
    private MovieService movieService;
    
    @Autowired
	private ScheduleRepository scheduleRepository;
    
    @Autowired
	private MovieRepository movieRepository;
    
    @ModelAttribute("schedule")
    public ScheduleDto movieScheduleDto() {
        return new ScheduleDto();
    }   
    
    @GetMapping
    public String showMovieScheduleForm1(Model model/*, @PathVariable ("movieTitle") String movieTitlel*/) {
    	Movie movie = movieService.findByTitle("11");
    	model.addAttribute("movie", movie);
        model.addAttribute("scheduleDate", new Schedule());
        return "movie-schedule";
    }

    @PostMapping
    public String addMovieSchedule(
    		Schedule schedule, Model model, BindingResult result) {
    	if(result.hasErrors()) {
            return "movie-schedule";
        }
    	
    	schedule.setMovie(movieRepository.findByTitle("11"));
        model.addAttribute("schedule", schedule);
    	scheduleRepository.save(schedule) ;
        return "redirect:/movie-schedule?success";
    }
    
}