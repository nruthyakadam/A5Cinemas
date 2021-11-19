package com.a5cinemas.user.dto;

import java.util.Date;

public class ScheduleDto {

	private String scheduleDate;

	private Date scheduleTime;

	public String getScheduleDate() {
		return scheduleDate;
	}

	public void setScheduleDate(String scheduleDate) {
		this.scheduleDate = scheduleDate;
	}

	public Date getScheduleTime() {
		return scheduleTime;
	}

	public void setScheduleTime(Date scheduleTime) {
		this.scheduleTime = scheduleTime;
	}

}
