package com.a5cinemas.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.a5cinemas.user.dto.PromoCreationDto;
import com.a5cinemas.user.model.Promotion;

public interface PromoService {

    Promotion save(@Valid PromoCreationDto promoCreationDto);

	public List<Promotion> findAll();
	
	public Promotion findByCode(String promoCode);

	Promotion sendPromos(@Valid PromoCreationDto promoCreationDto, String siteURL) throws UnsupportedEncodingException, MessagingException;
}
