package com.museum.management.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.museum.management.dto.IncidentDto;

@Validated
public interface IncidentService {

	@Transactional(readOnly = true)
	List<IncidentDto> findAll();

	@Transactional(readOnly = true)
	IncidentDto getIncident(@NotNull Integer id);

	@Transactional
	Integer createIncident(@NotNull @Valid IncidentDto incidentDto);

	@Transactional
	void updateIncident(@NotNull @Valid IncidentDto incidentDto, @NotNull Integer id);

	void deleteIncident(@NotNull Integer id);
	
	@Transactional(readOnly = true)
	List<IncidentDto> getMyIncident(String username);
}
