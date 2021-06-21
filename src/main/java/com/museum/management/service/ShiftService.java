package com.museum.management.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.museum.management.dto.ShiftDto;

@Validated
public interface ShiftService {

	@Transactional(readOnly = true)
	List<ShiftDto> findAll();

	@Transactional(readOnly = true)
	ShiftDto getShift(@NotNull Integer id);

	@Transactional
	Integer createShift(@NotNull @Valid ShiftDto shiftDto);

	@Transactional
	void updateShift(@NotNull @Valid ShiftDto shiftDto, @NotNull Integer id);

	void deleteShift(@NotNull Integer id);
	
	@Transactional(readOnly = true)
	List<ShiftDto> getMyShifts(String username);
}
