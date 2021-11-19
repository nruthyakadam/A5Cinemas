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
    
    @Autowired
	private ScheduleRepository scheduleRepository;
    
    
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
    
    @GetMapping("/{movieTitle}/find")
    public String findMovieByTitle(Model model, @PathVariable ("movieTitle") String movieTitle) {

        Movie movie = movieService.findByTitle(movieTitle);
        List<Schedule> schedules = scheduleRepository.findByMovieId(movie.getId());
        model.addAttribute("movie", movie);
        model.addAttribute("schedules", schedules);
        return "select-time"; //change html name
    }
    
    @GetMapping("/{category}/findByCategory")
    public String findMovieByCategory(Model model, @PathVariable ("category") String category) {
    	
        Movie movie = movieService.findByCategory(category);
        List<Schedule> schedules = scheduleRepository.findByMovieId(movie.getId());
        model.addAttribute("movie", movie);
        model.addAttribute("schedules", schedules);
        return "select-time";
    }
    
    @RequestMapping(path = {"/","/searchByTitle"})
    public String searchByTitle(Model model,
    		@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto,
        BindingResult result, String keyword) {
        List<Movie> movies = movieRepository.findByKeyword(keyword);

//        List<Schedule> schedules = scheduleRepository.findAll();
//    	List<Movie> moviesByKeyword = movieRepository.findByKeyword(keyword);
//    	List<Movie> currentMovies = new ArrayList<>();
//    	Set<Movie> movieSet = new HashSet<>();
//    	if(null != schedules) {
//    		for(Schedule schedule: schedules) {
//    			if(null != schedule.getMovie()) {
//    				currentMovies.add(schedule.getMovie());
//    			}
//    		}
//    	}
//    	
//    	model.addAttribute("currentMovies", movieSet);
//    	
//    	List<Movie> upcomingMovies = movieRepository.findByKeyword(keyword);
//    	if(null != schedules) {
//    		for(Movie movie : movieSet) {
//    			if(upcomingMovies.contains(movie)) {
//    				upcomingMovies.remove(movie);
//    			}
//    		}
//    	}
//    	model.addAttribute("upcomingMovies", upcomingMovies);
//       
//        if(movies.size() == 0) {
//        	return  "redirect:/index?success";
//        }
//        
        return "index";
    }
    
    @RequestMapping(path = {"/","/searchByCategory"})
    public String searchByCategory(Model model,
    		@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto,
        BindingResult result, String keyword) {
        List<Movie> movies = movieRepository.findByCategoryKeyword(movieCreationDto.getGenre());
        model.addAttribute("movies", movies);
        if(movies.size() == 0) {
        	return  "redirect:/index?failure";
        }
        return "index";
    }
    
}