package com.a5.cinemas.model;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Collections;

@Data
@Entity
@Table(name = "user")
public class AppUser implements UserDetails {

    /**
	 * generated
	 */
	private static final long serialVersionUID = 3115367623241784850L;

	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(name = "login")
    @Size(min = 4, message = "Username must be at least 4 characters long")
    @Size(max = 50, message = "Username can be up to 50 characters long")
    private String username;

    @Size(min = 6, message = "Password must be at least 6 characters long")
    private String password;

    private String role;

    @NotBlank(message = "Please provide the email address")
    @Email(message = "Please enter a valid email address")
    private String email;

    @Pattern(regexp = "\\d{10}", message = "Enter your 10-digits phone number")
    private String phone;

    @Size(max = 50, message = "Enter up to 50 characters")
    @NotBlank(message = "Please provide your first name")
    private String name;

    @Size(max = 50, message = "Enter up to 50 characters")
    @NotBlank(message = "Please provide your last name")
    private String surname;

    @Column(name = "active")
    private boolean isEnabled;

    public AppUser() {
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void setEnabled(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}

}