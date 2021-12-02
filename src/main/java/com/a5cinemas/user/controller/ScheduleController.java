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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.a5cinemas.user.dto.ScheduleDto;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.Repertoire;
import com.a5cinemas.user.model.Schedule;
import com.a5cinemas.user.repo.MovieRepository;
import com.a5cinemas.user.repo.RepertoireRepo;
import com.a5cinemas.user.repo.ScheduleRepository;
import com.a5cinemas.user.service.MovieService;



@Controller
@RequestMapping("/movies")
public class ScheduleController {

    @Autowired
    private MovieService movieService;
    
    @Autowired
	private ScheduleRepository scheduleRepository;
    
    @Autowired
    private RepertoireRepo repertoireRepo;
    
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
    
   // @RequestMapping(path = {"/admin","/admin/{movieName}/newRepertoire"})
    @GetMapping("/admin/{movieName}/newRepertoire")
    public String showMovieRepertoireForm(Model model, @PathVariable ("movieName") String movieName) {

        Movie movieRepertoire = movieRepository.findByTitle(movieName);
        model.addAttribute("movieRepertoire", movieRepertoire);
        model.addAttribute("repertoire", new Repertoire());
        return "movie-repertoire";
    }

    @PostMapping("/admin/newRepertoire")
    @Transactional
    public String addMovieRepertoire(@ModelAttribute ("repertoire") Repertoire repertoire,
                                @ModelAttribute("movieId") Long movieId, BindingResult result) {

        repertoire.setMovie(movieRepository.getById(movieId));
        repertoireRepo.save(repertoire) ;
        return "redirect:/movies/admin/"+repertoire.getMovie().getTitle()+"/newRepertoire?success";
    }
    
    @GetMapping("/admin/{movieName}/updateRepertoire/{repertoireId}")
    public String showUpdateMovieRepertoireForm(Model model, @PathVariable ("movieName") String movieName,
                                           @PathVariable("repertoireId") Long repertoireId) {

        Repertoire repertoire = repertoireRepo.getById(repertoireId);
        Movie movieRepertoire = movieRepository.findByTitle(movieName);
        model.addAttribute("movieRepertoire", movieRepertoire);
        model.addAttribute("repertoire", repertoire);
        return "movie-repertoire";
    }

    @PostMapping("/admin/updateRepertoire")
    @Transactional
    public String updateMovieRepertoire(@ModelAttribute ("repertoire") Repertoire repertoire,
                                @ModelAttribute("movieId") Long movieId,
                                @ModelAttribute("repertoireId") Long repertoireId, BindingResult result) {

        Repertoire repertoireFromDb = repertoireRepo.getById(repertoireId);
        repertoireFromDb.setDate(repertoire.getDate());
        return "redirect:/movies/list";
    }

    @GetMapping("/admin/deleteRepertoire/{repertoireId}")
    @Transactional
    public String deleteMovieRepertoire(Model model, @PathVariable("repertoireId") Long repertoireId) {

        repertoireRepo.deleteById(repertoireId);
        return "redirect:/movies/list";
    }
}