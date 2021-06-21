package com.museum.management.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.museum.management.dto.ExhibitDto;

@Validated
public interface ExhibitService {

	@Transactional(readOnly = true)
	List<ExhibitDto> findAll();

	@Transactional(readOnly = true)
	ExhibitDto getExhibit(@NotNull Integer id);

	@Transactional
	Integer createExhibit(@NotNull @Valid ExhibitDto exhibitDto);

	@Transactional
	void updateExhibit(@NotNull @Valid ExhibitDto exhibitDto, @NotNull Integer id);

	void deleteExhibit(@NotNull Integer id);
}
