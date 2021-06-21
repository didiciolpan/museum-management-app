package com.museum.management.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.museum.management.domain.Role;
import com.museum.management.domain.RoleRepository;
import com.museum.management.dto.RoleDto;
import com.museum.management.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

	private final RoleRepository roleRepository;
	private final ModelMapper modelMapper;

	public RoleServiceImpl(RoleRepository roleRepository, ModelMapper modelMapper) {
		this.roleRepository = roleRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public List<RoleDto> findAll() {
		return roleRepository.findAll().stream().map(entity -> modelMapper.map(entity, RoleDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public RoleDto getRole(@NotNull Integer id) {
		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Role not found!"));
		return modelMapper.map(role, RoleDto.class);
	}

	@Override
	public Integer createRole(@NotNull @Valid RoleDto roleDto) {
		Role role = modelMapper.map(roleDto, Role.class);
		role = roleRepository.save(role);
		return role.getId();
	}

	@Override
	public void updateRole(@NotNull @Valid RoleDto roleDto, @NotNull Integer id) {
		Role role = roleRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found!"));

		modelMapper.map(roleDto, role);
		roleRepository.save(role);
	}

	@Override
	public void deleteRole(@NotNull Integer id) {
		roleRepository.findById(id).ifPresent(roleRepository::delete);

	}

}
