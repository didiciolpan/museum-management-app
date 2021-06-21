package com.museum.management.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class ShiftDto {

	private Integer id;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate shiftDate;

	@NotNull
	private Integer workingHours;

	private UserDto user;

	private Integer selectedUserId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public LocalDate getShiftDate() {
		return shiftDate;
	}

	public void setShiftDate(LocalDate shiftDate) {
		this.shiftDate = shiftDate;
	}

	public Integer getWorkingHours() {
		return workingHours;
	}

	public void setWorkingHours(Integer workingHours) {
		this.workingHours = workingHours;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public Integer getSelectedUserId() {
		return selectedUserId;
	}

	public void setSelectedUserId(Integer selectedUserId) {
		this.selectedUserId = selectedUserId;
	}

}
