package com.museum.management.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.museum.management.domain.Shift;
import com.museum.management.domain.ShiftRepository;
import com.museum.management.domain.User;
import com.museum.management.dto.ShiftDto;
import com.museum.management.service.ShiftService;

@Service
public class ShiftServiceImpl implements ShiftService {

	private final ShiftRepository shiftRepository;
	private final ModelMapper modelMapper;

	public ShiftServiceImpl(ShiftRepository shiftRepository, ModelMapper modelMapper) {
		this.shiftRepository = shiftRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<ShiftDto> findAll() {
		return shiftRepository.findAll().stream().map(entity -> modelMapper.map(entity, ShiftDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public ShiftDto getShift(@NotNull Integer id) {
		Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Shift not found!"));
		return modelMapper.map(shift, ShiftDto.class);
	}

	@Override
	public Integer createShift(@NotNull @Valid ShiftDto shiftDto) {
		Shift shift = modelMapper.map(shiftDto, Shift.class);
		shift = shiftRepository.save(shift);
		return shift.getId();
	}

	@Override
	public void updateShift(@NotNull @Valid ShiftDto shiftDto, @NotNull Integer id) {
		Shift shift = shiftRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found!"));
		shift.setUser(new User());
		modelMapper.map(shiftDto, shift);
		shiftRepository.save(shift);
	}

	@Override
	public void deleteShift(@NotNull Integer id) {
		shiftRepository.findById(id).ifPresent(shiftRepository::delete);
	}

	@Override
	public List<ShiftDto> getMyShifts(String username) {
		return shiftRepository.getMyShifts(username).stream().map(entity -> modelMapper.map(entity, ShiftDto.class))
				.collect(Collectors.toList());
	}

}
