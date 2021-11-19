package com.a5cinemas.user.dto;

import java.time.LocalDateTime;

import javax.validation.constraints.NotEmpty;

public class PromoCreationDto {

	@NotEmpty
	private String code;

	@NotEmpty
	private LocalDateTime expiration;
	
	private Boolean sentPromo;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public LocalDateTime getExpiration() {
		return expiration;
	}

	public void setExpiration(LocalDateTime expiration) {
		this.expiration = expiration;
	}

	public Boolean getSentPromo() {
		return sentPromo;
	}

	public void setSentPromo(Boolean sentPromo) {
		this.sentPromo = sentPromo;
	}
	
}