package com.a5cinemas.user.controller;


import static java.lang.Boolean.TRUE;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import javax.mail.MessagingException;
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
import com.a5cinemas.user.dto.PromoCreationDto;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.Promotion;
import com.a5cinemas.user.model.Repertoire;
import com.a5cinemas.user.model.Reservation;
import com.a5cinemas.user.model.Reserve;
import com.a5cinemas.user.model.SeatReservation;
import com.a5cinemas.user.model.Ticket;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.repo.MovieRepository;
import com.a5cinemas.user.repo.RepertoireRepo;
import com.a5cinemas.user.repo.ReservationRepo;
import com.a5cinemas.user.repo.TicketRepo;
import com.a5cinemas.user.repo.UserRepository;
import com.a5cinemas.user.service.BookingService;
import com.a5cinemas.user.service.UserService;

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
    private UserService userService;
    
    @Autowired
    private BookingService bookingService;
    
    @ModelAttribute("reservation")
    public Reservation reservation() {
        return new Reservation();
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
            reservation.setTicketPrice(0.00+reservedSeats.size()*15);
            reservation.setCost(8.76+reservedSeats.size()*15);
            Reservation reservation2 = reservationRepo.save(reservation);
            return "redirect:/order-summary/"+reservation.getReservationId()+"/"+reservation.getCost()+"/"+reservation.getTicketPrice()+"/"+reservation.getRepertoire().getDate()+"/"+reservation.getTicket().getSeat();
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
    
    @GetMapping("/order-summary/{reservationId}/{cost}/{price}/{date}/{seats}")
    public String getOrder(Model model, @PathVariable("cost") Double cost, @PathVariable("price") Double price,
    		@PathVariable("seats") String seats, @PathVariable("date") String date, @PathVariable("reservationId") Long reservationId) {
    	Reservation reservation1 = reservationRepo.findByReservationId(reservationId);
    	model.addAttribute("cost", String.format("%.2f", cost));
    	model.addAttribute("price", String.format("%.2f", price));
        model.addAttribute("seats",seats);
        model.addAttribute("date", date);
        model.addAttribute("reservationId", reservationId);
        return "order_summary";
    }
    
    @PostMapping("/payment/{reservationId}")
    public String makePayment(Model model, @ModelAttribute("reservationId") String reservationId) {

    	Reservation reservation = reservationRepo.findByReservationId(Long.parseLong(reservationId));
        model.addAttribute("reservation", reservation);
        model.addAttribute("rows", rows);
        return "redirect:/checkout/"+reservationId;
    }
    
    @GetMapping("/checkout/{reservationId}")
    public String checkout(Model model, @PathVariable("reservationId") String reservationId, Principal principal) {
    	Reservation reservation = reservationRepo.findByReservationId(Long.parseLong(reservationId));
    	CinemaUserDetails user = userService.findByEmail(principal.getName());
    	model.addAttribute("reservation", reservation);
    	model.addAttribute("user", user.getUser());
    	model.addAttribute("cost", String.format("%.2f", reservation.getCost()));
        return "checkout-form";
    }
    
    @PostMapping("/payment_confirnation/{reservationId}")
    public String addNewPromo(Model model, @ModelAttribute("reservationId") String reservationId, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
    	Reservation reservation = reservationRepo.findByReservationId(Long.parseLong(reservationId));
    	bookingService.sendConfirmation(reservation, getSiteURL(request));
        return "redirect:/payment-success";
    }
    
    @GetMapping("/payment-success")
    public String paymentSuccess(Model model) {
         
        return "payment_success";
    }
    
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
    
//    @PostMapping("/apply-promo/{reservationId}/{promo}")
//    public String applyPromo(@ModelAttribute("promo") Promotion promo, @ModelAttribute("reservationId") String reservationId) {
//    	Reservation reservation = reservationRepo.findByReservationId(Long.parseLong(reservationId));
//    	Double newCost = reservation.getCost() - promo.getDisountPercentage();
//    	reservation.setCost(newCost);
//		return "checkout-form";
//    }
    
   // @GetMapping("confirmation")
}