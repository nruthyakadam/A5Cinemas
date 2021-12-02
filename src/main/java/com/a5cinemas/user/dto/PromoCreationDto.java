package com.a5cinemas.user.dto;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

public class PromoCreationDto {

	@NotEmpty
	private String code;

	private Boolean sentPromo;

	private String description;

	private LocalDateTime expiryDate;

	private int disountPercentage;

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Boolean getSentPromo() {
		return sentPromo;
	}

	public void setSentPromo(Boolean sentPromo) {
		this.sentPromo = sentPromo;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getExpiryDate() {
		return expiryDate;
	}

	public void setExpiryDate(LocalDateTime expiryDate) {
		this.expiryDate = expiryDate;
	}

	public int getDisountPercentage() {
		return disountPercentage;
	}

	public void setDisountPercentage(int disountPercentage) {
		this.disountPercentage = disountPercentage;
	}

}