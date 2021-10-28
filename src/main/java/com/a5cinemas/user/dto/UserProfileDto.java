package com.a5cinemas.user.dto;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;


public class UserProfileDto {

    @NotEmpty
    private String firstName;

    @NotEmpty
    private String lastName;

    private String password;

    private String oldPassword;
    @Email
    @NotEmpty
    private String email;
    
    
    private Boolean recievePromotion = Boolean.FALSE;
	
	private String address;
	
	private String card;


    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isRecievePromotion() {
		return recievePromotion;
	}

	public void setRecievePromotion(Boolean recievePromotion) {
		this.recievePromotion = recievePromotion;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getCard() {
		return card;
	}

	public void setCard(String card) {
		this.card = card;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
}