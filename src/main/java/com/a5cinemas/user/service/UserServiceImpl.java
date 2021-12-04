package com.a5cinemas.user.service;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.a5cinemas.user.dto.UserProfileDto;
import com.a5cinemas.user.dto.UserRegistrationDto;
import com.a5cinemas.user.exception.CustomerNotFoundException;
import com.a5cinemas.user.model.CinemaUserDetails;
import com.a5cinemas.user.model.Role;
import com.a5cinemas.user.model.User;
import com.a5cinemas.user.repo.UserRepository;

import net.bytebuddy.utility.RandomString;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private JavaMailSender mailSender;

	public CinemaUserDetails findByEmail(String email) {
		User user = userRepository.findByEmail(email);

		return new CinemaUserDetails(user);
	}

	public User save(UserRegistrationDto registration, String siteURL)
			throws UnsupportedEncodingException, MessagingException {
		User user = new User();
		user.setFirstName(registration.getFirstName());
		user.setLastName(registration.getLastName());
		user.setEmail(registration.getEmail());
		user.setPassword(passwordEncoder.encode(registration.getPassword()));
		user.setAddress(registration.getAddress());
		user.setCity(registration.getCity());
		user.setZipcode(registration.getZipcode());
		if(!StringUtils.isEmpty(registration.getCard())){
		user.setCard(passwordEncoder.encode(registration.getCard()));}
		user.setCardName(registration.getCardName());
		user.setRecievePromotion(registration.getRecievePromotion());
		user.setRoles(Arrays.asList(new Role("USER")));
		String randomCode = RandomString.make(64);
		user.setVerificationCode(randomCode);
		user.setEnabled(Boolean.FALSE);
		User savedUser = userRepository.save(user);
		sendVerificationEmail(user, siteURL);
		return savedUser;
	}

	public User save(UserProfileDto profileDto)
			throws UnsupportedEncodingException, MessagingException, CustomerNotFoundException {
		User user = userRepository.findByEmail(profileDto.getEmail());
		user.setFirstName(profileDto.getFirstName());
		user.setLastName(profileDto.getLastName());
		user.setEmail(profileDto.getEmail());
//        User existingUser = userRepository.findByEmail(profileDto.getEmail());
//        if(null!= profileDto.getOldPassword() && existingUser.getPassword().equals(passwordEncoder.encode(profileDto.getOldPassword()))){
//        	user.setPassword(passwordEncoder.encode(profileDto.getNewPassword()));
//        }else {
//            throw new CustomerNotFoundException("Old password is incorrect or empty");
//        }
		if(null!=profileDto.getPassword() && !profileDto.getPassword().isBlank()
				&& (null == profileDto.getOldPassword() || profileDto.getOldPassword().isBlank() || 
						!passwordEncoder.matches(profileDto.getOldPassword(), user.getPassword()))) {
			throw new CustomerNotFoundException("The old passwrd is incorrect. ");
		}else if (null!=profileDto.getPassword() && passwordEncoder.matches(profileDto.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(profileDto.getPassword()));
		}
		
		user.setAddress(profileDto.getAddress());
		user.setCard(passwordEncoder.encode(profileDto.getCard()));
		user.setRecievePromotion(profileDto.isRecievePromotion());
		User savedUser = userRepository.save(user);
		sendEditConirmationMail(user);
		
		return savedUser;
	}

	public User get(long id) {
		return userRepository.findById(id).get();
	}

	private void sendEditConirmationMail(User user)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "support@a5cinemas.com";
		String senderName = "A5 Cinemas Support";
		String subject = "Profile information changed";
		String content = "Hello [[name]],<br><br>" + "This is to let you know that your profile information has been updated.<br><br>"
				+ "Thanks and Regards,<br>"
				+ "A5 Cimeas<br>" + "Ph: (706)-714-XXXX<br>" + "Add.: Lakeside Dr, Athens-30605";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());

		helper.setText(content, true);

		mailSender.send(message);

	}
	
	private void sendVerificationEmail(User user, String siteURL)
			throws MessagingException, UnsupportedEncodingException {
		String toAddress = user.getEmail();
		String fromAddress = "support@a5cinemas.com";
		String senderName = "A5 Cinemas Support";
		String subject = "Please verify your registration";
		String content = "Hello [[name]],<br><br>" + "Please click the link below to verify your registration:<br>"
				+ "<h3><a href=\"[[URL]]\" target=\"_self\">Verification Link</a></h3>" + "Thanks and Regards,<br>"
				+ "A5 Cimeas<br>" + "Ph: (706)-714-XXXX<br>" + "Add.: Lakeside Dr, Athens-30605";

		MimeMessage message = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message);

		helper.setFrom(fromAddress, senderName);
		helper.setTo(toAddress);
		helper.setSubject(subject);

		content = content.replace("[[name]]", user.getFirstName() + " " + user.getLastName());
		String verifyURL = siteURL + "/verify?code=" + user.getVerificationCode();

		content = content.replace("[[URL]]", verifyURL);

		helper.setText(content, true);

		mailSender.send(message);

	}

	public Boolean verify(String verificationCode) {
		User user = userRepository.findByVerificationCode(verificationCode);

		if (user == null || user.isEnabled()) {
			return Boolean.FALSE;
		} else {
			user.setVerificationCode(null);
			user.setEnabled(true);
			userRepository.save(user);

			return Boolean.TRUE;
		}
	}

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user == null) {
			throw new UsernameNotFoundException("Invalid username or password.");
		}else if(!user.isEnabled()) {
			throw new UsernameNotFoundException("Please click on the verification link sent to your email.");
		}
		return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
				mapRolesToAuthorities(user.getRoles()));
	}

	private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
		return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
	}

	public void updateResetPasswordToken(String token, String email) throws CustomerNotFoundException {
		User user = userRepository.findByEmail(email);
		if (user != null) {
			user.setResetPasswordToken(token);
			userRepository.save(user);
		} else {
			throw new CustomerNotFoundException("Could not find any customer with the email " + email);
		}
	}

	public User getByResetPasswordToken(String token) {
		return userRepository.findByResetPasswordToken(token);
	}

	public void updatePassword(User user, String newPassword) {
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedPassword = passwordEncoder.encode(newPassword);
		user.setPassword(encodedPassword);

		user.setResetPasswordToken(null);
		userRepository.save(user);
	}

}