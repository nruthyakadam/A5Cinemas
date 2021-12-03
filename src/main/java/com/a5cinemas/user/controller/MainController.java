package com.a5cinemas.user.controller;

import java.io.UnsupportedEncodingException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.a5cinemas.user.dto.UserProfileDto;
import com.a5cinemas.user.exception.CustomerNotFoundException;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.Movie;
import com.a5cinemas.user.model.Repertoire;
import com.a5cinemas.user.model.Schedule;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.repo.MovieRepository;
import com.a5cinemas.user.repo.RepertoireRepo;
import com.a5cinemas.user.repo.ScheduleRepository;
import com.a5cinemas.user.service.MovieService;
import com.a5cinemas.user.service.UserService;

@Controller
public class MainController {

	@Autowired
    private UserService userService;
	
	@Autowired
    private MovieService movieService;
	
	@Autowired
	private RepertoireRepo repertoireRepo;

	
	@GetMapping("/")
    public String root(Model model) {
    	
    	List<Repertoire> schedules = repertoireRepo.findAll();
    	Set<Movie> currentMovies = new HashSet<>();
    	if(null != schedules) {
    		for(Repertoire schedule: schedules) {
    			if(null != schedule.getMovie()) {
    				currentMovies.add(schedule.getMovie());
    			}
    		}
    	}
    	model.addAttribute("currentMovies", currentMovies);
    	
    	List<Movie> upcomingMovies = movieService.findAll();
    	if(null != schedules) {
    		for(Movie movie : currentMovies) {
    			if(upcomingMovies.contains(movie)) {
    				upcomingMovies.remove(movie);
    			}
    		}
    	}
    	model.addAttribute("upcomingMovies", upcomingMovies);
    	return "index";
    }

	@GetMapping("/index")
    public String index(Model model) {
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
    
//    @RequestMapping(value = "/save", method = RequestMethod.POST)
//    public String saveProduct(@ModelAttribute("user")  @Valid User userDto) {
//    	userDto.setId(26L);
//    	userService.save(userDto);
//         
//        return "redirect:/";
//    }

    
    
    @GetMapping("/account")
    public String viewUserAccountForm(
            @AuthenticationPrincipal CinemaUserDetails userDetails,
            Model model, HttpServletRequest request) {
        String userEmail = request.getUserPrincipal().getName();
        CinemaUserDetails user = userService.findByEmail(userEmail);
         
        model.addAttribute("user", user.getUser());
        model.addAttribute("pageTitle", "Account Details");
         
        return "edit-profile";
    }
    
    @PostMapping("/account/update")
    public String saveDetails(@ModelAttribute("user") @Valid UserProfileDto userDto, RedirectAttributes redirectAttributes, Model model) {
    	try {
    		User savedUser = userService.save(userDto);
    		model.addAttribute("user", savedUser);
		} catch (CustomerNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return  "redirect:/account?failure";
        } catch (UnsupportedEncodingException | MessagingException e) {
            model.addAttribute("error", "Error while sending email");
        }
    	 
    	//redirectAttributes.addFlashAttribute("message", "Your profile has been updated.");
    	return  "redirect:/account?success";
    }
}