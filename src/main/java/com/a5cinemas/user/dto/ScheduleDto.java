package com.a5cinemas.user.dto;


import java.time.LocalDateTime;

public class ScheduleDto {

	private LocalDateTime date;
    
    private LocalDateTime time;
    

	public LocalDateTime getDate() {
		return date;
	}

	public void setDate(LocalDateTime date) {
		this.date = date;
	}

	public LocalDateTime getTime() {
		return time;
	}

	public void setTime(LocalDateTime time) {
		this.time = time;
	}



}
