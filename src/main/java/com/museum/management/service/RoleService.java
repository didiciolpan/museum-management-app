package com.museum.management.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.museum.management.dto.RoleDto;

@Validated
public interface RoleService {

	@Transactional(readOnly = true)
	List<RoleDto> findAll();

	@Transactional(readOnly = true)
	RoleDto getRole(@NotNull Integer id);

	@Transactional
	Integer createRole(@NotNull @Valid RoleDto roleDto);
	
	@Transactional
    void updateRole(@NotNull @Valid RoleDto roleDto, @NotNull Integer id);
	
	void deleteRole(@NotNull Integer id);

}
