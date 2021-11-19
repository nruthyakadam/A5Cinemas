package com.a5cinemas.user.controller;

import java.io.UnsupportedEncodingException;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.a5cinemas.user.dto.PromoCreationDto;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.Promotion;
import com.a5cinemas.user.service.PromoService;

@Controller
public class PromotionsController {

    @Autowired
    private PromoService promoService;
    
    @GetMapping("manage-promotions")
    public String manageMovies() {
        return "manage-promo";
    }
    
    @GetMapping("promotions_list")
    public String getPromos(Model model) {
        model.addAttribute("promos", promoService.findAll());
        return "promos";
    }
    
    @GetMapping("/add_promo")
    public String showPromonForm(Model model) {
        return "manage-promo";
    }

    @PostMapping("/add_promo")
    public String addNewPromo(@ModelAttribute("promo") @Valid PromoCreationDto promoCreationDto,
        BindingResult result) {
    	if(result.hasErrors()) {
            return "manage-promo";
        }
    	Promotion existing = promoService.findByCode(promoCreationDto.getCode());
    	if (existing != null) {
    		result.rejectValue("code", null, "There is already a promotion with that code.");
    	}

    	if (result.hasErrors()) {
    		return "promos";
    	}
    	promoService.save(promoCreationDto);
        return "redirect:/creation?success"; //TODO: add success msg in creation.html like registration.html OR return "redirect:/list"
    }
    
    @PostMapping("/send_promo")
    public String sendPromotionEmails(@ModelAttribute("promo") @Valid PromoCreationDto promoCreationDto,
        BindingResult result, HttpServletRequest request) throws UnsupportedEncodingException, MessagingException {
        promoService.sendPromos(promoCreationDto, getSiteURL(request));
        return "redirect:/promos?success";
    }
    
    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    } 
}