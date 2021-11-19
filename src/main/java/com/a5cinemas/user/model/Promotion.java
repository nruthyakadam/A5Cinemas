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
	
}
