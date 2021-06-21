package com.museum.management.dto;

import java.time.LocalDate;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import lombok.Data;

@Data
public class IncidentDto {

	private Integer id;

	@NotEmpty
	private String incidentNumber;

	@NotNull
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate incidentDate;

	@NotEmpty
	private String description;

	private UserDto user;

	private ExhibitDto exhibit;

	private Integer selectedExhibitId;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIncidentNumber() {
		return incidentNumber;
	}

	public void setIncidentNumber(String incidentNumber) {
		this.incidentNumber = incidentNumber;
	}

	public LocalDate getIncidentDate() {
		return incidentDate;
	}

	public void setIncidentDate(LocalDate incidentDate) {
		this.incidentDate = incidentDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}

	public ExhibitDto getExhibit() {
		return exhibit;
	}

	public void setExhibit(ExhibitDto exhibit) {
		this.exhibit = exhibit;
	}

	public Integer getSelectedExhibitId() {
		return selectedExhibitId;
	}

	public void setSelectedExhibitId(Integer selectedExhibitId) {
		this.selectedExhibitId = selectedExhibitId;
	}

}
