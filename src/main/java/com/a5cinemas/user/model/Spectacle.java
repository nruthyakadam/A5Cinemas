package com.a5cinemas.user.model;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Table(name = "spectacle")
public class Spectacle {

    @GeneratedValue(strategy = GenerationType.AUTO)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private Integer lenght;

    private Integer minAge;
    @Column(columnDefinition = "TEXT")
    private String imageUrl;

    @OneToMany(mappedBy = "spectacle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Reservation> spectacleReservations;

    @OneToMany(mappedBy = "spectacle", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Repertoire> spectacleRepertoires;

    public Spectacle() {
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getLenght() {
		return lenght;
	}

	public void setLenght(Integer lenght) {
		this.lenght = lenght;
	}

	public Integer getMinAge() {
		return minAge;
	}

	public void setMinAge(Integer minAge) {
		this.minAge = minAge;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public List<Reservation> getSpectacleReservations() {
		return spectacleReservations;
	}

	public void setSpectacleReservations(List<Reservation> spectacleReservations) {
		this.spectacleReservations = spectacleReservations;
	}

	public List<Repertoire> getSpectacleRepertoires() {
		return spectacleRepertoires;
	}

	public void setSpectacleRepertoires(List<Repertoire> spectacleRepertoires) {
		this.spectacleRepertoires = spectacleRepertoires;
	}
}