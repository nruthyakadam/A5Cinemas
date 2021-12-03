package com.a5cinemas.user.service;


import java.io.UnsupportedEncodingException;
import java.time.format.DateTimeFormatter;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a5cinemas.user.dto.PromoCreationDto;
import com.a5cinemas.user.model.Promotion;
import com.a5cinemas.user.model.Reservation;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.repo.PromoRepository;
import com.a5cinemas.user.repo.UserRepository;

@Service
@Transactional
public class BookingServiceImpl implements BookingService {

	@Autowired
	private PromoRepository promoRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void sendConfirmation(@Valid Reservation reservation, String siteURL) throws UnsupportedEncodingException, MessagingException {
		String fromAddress = "support@a5cinemas.com";
		String senderName = "A5 Cinemas Support";
		String subject = "Booking Confirmation";
		String content = "Hello,<br><br>" + "Thank you for choosing A5. Please find below the details of your ticket confirmation.: <br>"
				+ "Confirmation Number: [[reservationId]]<br>"
				+ "Movie: [[movieId]]<br>"
				+ "Date: [[date]]<br>"
				+ "Seats: [[seats]]<br>"
				+ "Cost: [[cost]]<br>"
				+ "Thanks and Regards,<br>"
				+ "A5 Cimeas<br>" + "Ph: (706)-714-XXXX<br>" + "Add.: Lakeside Dr, Athens-30605";

		StringBuilder toAddresses = new StringBuilder();
		toAddresses.append(reservation.getUser().getEmail());
		
		
		MimeMessage message = mailSender.createMimeMessage();
		InternetAddress[] parse = InternetAddress.parse(toAddresses.toString() , true);
		message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setSubject(subject);
		content = content.replace("[[reservationId]]", reservation.getReservationId().toString());
		content = content.replace("[[movieId]]", reservation.getMovie().getTitle());
		//DateTimeFormatter FOMATTER = DateTimeFormatter.ofPattern("MM/dd/yyyy 'at' hh:mm a z");
		content = content.replace("[[date]]", reservation.getRepertoire().getDate().toString());
		content = content.replace("[[seats]]", reservation.getTicket().getSeat());
		content = content.replace("[[cost]]", String.format("%.2f", reservation.getCost()));
		helper.setText(content, true);

		mailSender.send(message);
	}

}