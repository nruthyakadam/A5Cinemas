package com.a5cinemas.user.service;


import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.a5cinemas.user.dto.PromoCreationDto;
import com.a5cinemas.user.model.Promotion;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.repo.PromoRepository;
import com.a5cinemas.user.repo.UserRepository;

@Service
@Transactional
public class PromoServiceImpl implements PromoService {

	@Autowired
	private PromoRepository promoRepository;
	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public List<Promotion> findAll() {
		return promoRepository.findAll();
	}

	@Override
	public Promotion save(@Valid PromoCreationDto promoCreationDto) {
		Promotion promotion = new Promotion();
		promotion.setCode(promoCreationDto.getCode());
		promotion.setExpiration(promoCreationDto.getExpiration());
		promotion.setSentPromo(promoCreationDto.getSentPromo());
		Promotion savedPromotion = promoRepository.save(promotion);
		return savedPromotion;
	}

	@Override
	public Promotion findByCode(String promoCode) {
		Promotion promotion = promoRepository.findByCode(promoCode);
		return promotion;
	}

	@Override
	public Promotion sendPromos(@Valid PromoCreationDto promoCreationDto, String siteURL) throws UnsupportedEncodingException, MessagingException {
		Promotion promotion = promoRepository.findByCode(promoCreationDto.getCode());
		promotion.setCode(promoCreationDto.getCode());
		promotion.setExpiration(promoCreationDto.getExpiration());
		promotion.setSentPromo(promoCreationDto.getSentPromo());
		Promotion savedPromo = promoRepository.save(promotion);
		sendPromotionEmails(savedPromo, siteURL);
		return savedPromo;
	}
	
	private void sendPromotionEmails(Promotion savedPromo, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		
		String fromAddress = "support@a5cinemas.com";
		String senderName = "A5 Cinemas Support";
		String subject = "Save 30% at A5 Cinemas";
		String content = "Hello [[name]],<br><br>" + "Take 30% (upto $20) off your first movie when you use promo code: [[promo]]<br>"
				+ "Thanks and Regards,<br>"
				+ "A5 Cimeas<br>" + "Ph: (706)-714-XXXX<br>" + "Add.: Lakeside Dr, Athens-30605";

		List<User> users = userRepository.findAll();
		String toAddresses = new String();
		for (User user : users) {
			if(user.getRecievePromotion()) {
				toAddresses.concat(user.getEmail()).concat(", ");
			}
		}
		
		MimeMessage message = mailSender.createMimeMessage();
		InternetAddress[] parse = InternetAddress.parse(toAddresses , true);
		message.setRecipients(javax.mail.Message.RecipientType.TO, parse);
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setSubject(subject);
		content = content.replace("[[promo]]", savedPromo.getCode());
		helper.setText(content, true);

		mailSender.send(message);

	}

}