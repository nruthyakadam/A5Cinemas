package com.a5cinemas.user.controller;


import static java.lang.Boolean.TRUE;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.a5cinemas.user.dto.MovieCreationDto;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.Repertoire;
import com.a5cinemas.user.model.Reservation;
import com.a5cinemas.user.model.Reserve;
import com.a5cinemas.user.model.SeatReservation;
import com.a5cinemas.user.model.Ticket;
import com.a5cinemas.user.repo.MovieRepository;
import com.a5cinemas.user.repo.RepertoireRepo;
import com.a5cinemas.user.repo.ReservationRepo;
import com.a5cinemas.user.repo.TicketRepo;
import com.a5cinemas.user.repo.UserRepository;

@Controller
public class BookTicketController {

	@Autowired
    private RepertoireRepo repertoireRepo;
	
	@Autowired
    private  TicketRepo ticketRepo;
	
	@Autowired
    private  MovieRepository movieRepo;
	
	@Autowired
    private  UserRepository userRepo;
	
	@Autowired
    private  ReservationRepo reservationRepo;
    
    @ModelAttribute("movie")
    public MovieCreationDto movieCreationDto() {
        return new MovieCreationDto();
    }
    
    @Autowired
   	private MovieRepository movieRepository;
    
    private static final List<String> rows = Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J");
    
    @GetMapping("/select-seats")
    public String selectTime(Model model) {
         
        return "select-seats";
    }
    
    @RequestMapping(path = {"/","/select-time"})
    public String selectTime(Model model,
    		@ModelAttribute("movie") @Valid MovieCreationDto movieCreationDto,
        BindingResult result, String title) {
        Movie movie = movieRepository.findByTitle(title);
        List<Repertoire> repertoires = repertoireRepo.findByMovieId(movie.getId());
        model.addAttribute("movie", movie);
        model.addAttribute("repertoires",repertoires);
        return "select-time";
    }
    
    @GetMapping("/movies/{movieName}/reservation/{repertoireId}")
    public String movieReservationSeatPage(Model model, @PathVariable("movieName") String movieName,
                                           @PathVariable("repertoireId") Long repertoireId) {

        reserve(model, repertoireId);
        model.addAttribute("movieName", movieName);
        model.addAttribute("repertoireId", repertoireId);
        model.addAttribute("rows", rows);
        return "select-seats";
    }
    
    private void reserve(Model model, @PathVariable("repertoireId") Long repertoireId) {
        Reserve reserveMovie = new Reserve();
        SeatReservation seatReservation = new SeatReservation();

        Map<String,Boolean> mapMovie = new HashMap<>();

        getReservedSeats(repertoireId).forEach(seat -> mapMovie.put(seat, true));

        reserveMovie.setMap(mapMovie);

        reserveMovie.setSeatReservation(seatReservation);
        model.addAttribute("seatInfo", reserveMovie);
    }


    private Set<String> getReservedSeats(@PathVariable("repertoireId") Long repertoireId) {
        Repertoire repertoire = repertoireRepo.getById(repertoireId);
        Set<String> reservedSeats = new HashSet<>();
        if(null!=repertoire.getReservations()) {
        for (Reservation reservation :  repertoire.getReservations()) {
            reservedSeats.addAll(Arrays.asList(reservation.getTicket().getSeat().split(",")));
        }
        }
        System.out.println(reservedSeats);
        return reservedSeats;
    }
    
    @PostMapping("/reservation/save/{repertoireId}")
    public String reserve(@ModelAttribute ("seatInfo") Reserve reserve, Principal principal,
                          @ModelAttribute("repertoireId") Long repertoireId) {

        List<String> reservedSeats = getReservedSeats(reserve);
        if (reservedSeats.size() > 0 && reservedSeats.size() <= 15) {
            UUID uuid = UUID.randomUUID();
            Ticket ticket = new Ticket();
            ticket.setSeat(String.join(",", reservedSeats));
            ticket.setUuid(uuid);
            ticketRepo.save(ticket);

            Reservation reservation = new Reservation();

            reservation.setTicket(ticket);
            Repertoire repertoire = repertoireRepo.findById(repertoireId).orElse(null);
//            try {
                if (repertoire != null) {
                    reservation.setMovie(movieRepo.findByTitle(repertoire.getMovie().getTitle()));
                }
//            } catch (NullPointerException e) {
//                if (repertoire.getSpectacle() != null) {
//                    reservation.setSpectacle(spectacleRepo.findByTitle(repertoire.getSpectacle().getTitle()));
//                }
//            }
            reservation.setRepertoire(repertoire);
            reservation.setUser(userRepo.findByEmail(principal.getName()));
            reservationRepo.save(reservation);
            return "redirect:/successful";
        } else {
            return "redirect:/unsuccessful";
        }
    }
    
    private List<String> getReservedSeats(@ModelAttribute("seatInfo") Reserve reserve) {
        List<String> reservedSeats = new ArrayList<>();
        for (Map.Entry<String, Boolean> entry : reserve.getMap().entrySet()) {
            if (TRUE.equals(entry.getValue())) {
                reservedSeats.add(entry.getKey());
            }
        }
        return reservedSeats;
    }
}