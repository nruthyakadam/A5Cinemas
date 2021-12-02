package com.a5cinemas.user.model;

import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "promotion")
public class Promotion {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "code", length = 255, nullable = false)
	private String code;
    
    @Column(name = "description", length = 255, nullable = false)
	private String description;
    
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
	@Column(name= "expiry_date", nullable = false, unique = true)
    private LocalDateTime expiryDate;
    
    @Column(name = "disc_percent")
    private int disountPercentage;
    
    public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@Column(name = "sent_promo")
	private Boolean sentPromo = Boolean.FALSE;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


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
