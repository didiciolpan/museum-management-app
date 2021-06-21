package com.museum.management.service;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import com.museum.management.dto.UserDto;

@Validated
public interface UserService {

	@Transactional(readOnly = true)
	List<UserDto> findAll();

	@Transactional(readOnly = true)
	UserDto getUser(@NotNull Integer id);

	@Transactional
	Integer createUser(@NotNull @Valid UserDto userDto);

	@Transactional
	void updateUser(@NotNull @Valid UserDto userDto, @NotNull Integer id);

	void deleteUser(@NotNull Integer id);

	@Transactional(readOnly = true)
	UserDto getUserByUsername(String username);
	
	@Transactional(readOnly = true)
	List<UserDto> fndByRole(String roleName);
}
