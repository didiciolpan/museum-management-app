package com.museum.management.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.museum.management.domain.Exhibit;
import com.museum.management.domain.ExhibitRepository;
import com.museum.management.dto.ExhibitDto;
import com.museum.management.service.ExhibitService;

@Service
public class ExhibitServiceImpl implements ExhibitService {

	private final ExhibitRepository exhibitRepository;
	private final ModelMapper modelMapper;

	public ExhibitServiceImpl(ExhibitRepository exhibitRepository, ModelMapper modelMapper) {
		this.exhibitRepository = exhibitRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ExhibitDto> findAll() {
		return exhibitRepository.findAll().stream().map(entity -> modelMapper.map(entity, ExhibitDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ExhibitDto getExhibit(@NotNull Integer id) {
		Exhibit exhibit = exhibitRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));

		return modelMapper.map(exhibit, ExhibitDto.class);
	}

	@Override
	public Integer createExhibit(@NotNull @Valid ExhibitDto exhibitDto) {
		Exhibit exhibit = modelMapper.map(exhibitDto, Exhibit.class);
		exhibit = exhibitRepository.save(exhibit);
		return exhibit.getId();
	}

	@Override
	public void updateExhibit(@NotNull @Valid ExhibitDto exhibitDto, @NotNull Integer id) {
		Exhibit exhibit = exhibitRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found!"));
		modelMapper.map(exhibitDto, exhibit);
		exhibitRepository.save(exhibit);

	}

	@Override
	public void deleteExhibit(@NotNull Integer id) {
		exhibitRepository.findById(id).ifPresent(exhibitRepository::delete);

	}

}
