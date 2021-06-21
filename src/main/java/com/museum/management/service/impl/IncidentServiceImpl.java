package com.museum.management.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.museum.management.domain.Exhibit;
import com.museum.management.domain.Incident;
import com.museum.management.domain.IncidentRepository;
import com.museum.management.domain.User;
import com.museum.management.dto.IncidentDto;
import com.museum.management.service.IncidentService;

@Service
public class IncidentServiceImpl implements IncidentService {

	private final IncidentRepository incidentRepository;
	private final ModelMapper modelMapper;

	public IncidentServiceImpl(IncidentRepository incidentRepository, ModelMapper modelMapper) {
		this.incidentRepository = incidentRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<IncidentDto> findAll() {
		return incidentRepository.findAll().stream().map(entity -> modelMapper.map(entity, IncidentDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public IncidentDto getIncident(@NotNull Integer id) {
		Incident incident = incidentRepository.findById(id)
				.orElseThrow(() -> new RuntimeException("Incident not found!"));
		return modelMapper.map(incident, IncidentDto.class);
	}

	@Override
	public Integer createIncident(@NotNull @Valid IncidentDto incidentDto) {
		Incident incident = modelMapper.map(incidentDto, Incident.class);
		incident = incidentRepository.save(incident);
		return incident.getId();
	}

	@Override
	public void updateIncident(@NotNull @Valid IncidentDto incidentDto, @NotNull Integer id) {
		Incident incident = incidentRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found!"));
		incident.setUser(new User());
		incident.setExhibit(new Exhibit());
		modelMapper.map(incidentDto, incident);
		incidentRepository.save(incident);
	}

	@Override
	public void deleteIncident(@NotNull Integer id) {
		incidentRepository.findById(id).ifPresent(incidentRepository::delete);
	}

	@Override
	public List<IncidentDto> getMyIncident(String username) {
		return incidentRepository.getMyIncidents(username).stream()
				.map(entity -> modelMapper.map(entity, IncidentDto.class)).collect(Collectors.toList());
	}

}
