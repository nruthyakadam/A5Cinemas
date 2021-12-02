package com.a5cinemas.user.model;

import lombok.Data;
import java.util.Map;

@Data
public class Reserve {

    private SeatReservation seatReservation;
    private Map<String,Boolean> map;

    private Long id;
    private String string;
    private boolean active;

    public boolean isActive() {
        return active;
    }

	public SeatReservation getSeatReservation() {
		return seatReservation;
	}

	public void setSeatReservation(SeatReservation seatReservation) {
		this.seatReservation = seatReservation;
	}

	public Map<String, Boolean> getMap() {
		return map;
	}

	public void setMap(Map<String, Boolean> map) {
		this.map = map;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getString() {
		return string;
	}

	public void setString(String string) {
		this.string = string;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}
