package com.a5cinemas.user.model;

import lombok.Data;

@Data
public class SeatReservation {

    private String seat;
    private boolean active;

    public boolean isActive() {
        return active;
    }

	public String getSeat() {
		return seat;
	}

	public void setSeat(String seat) {
		this.seat = seat;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
