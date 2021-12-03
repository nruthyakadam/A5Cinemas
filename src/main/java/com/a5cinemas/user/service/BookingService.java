package com.a5cinemas.user.service;

import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import com.a5cinemas.user.dto.PromoCreationDto;
import com.a5cinemas.user.model.Promotion;
import com.a5cinemas.user.model.Reservation;

public interface BookingService {

	void sendConfirmation(@Valid Reservation reservation, String siteURL) throws UnsupportedEncodingException, MessagingException;

}
