package com.museum.management.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.museum.management.domain.Role;
import com.museum.management.domain.User;
import com.museum.management.domain.UserRepository;
import com.museum.management.dto.UserDto;
import com.museum.management.service.UserService;

@Service
public class UserServiceImpl implements UserDetailsService, UserService {

	private final UserRepository userRepository;
	private final ModelMapper modelMapper;

	public UserServiceImpl(UserRepository userRepository, ModelMapper modelMapper) {
		this.userRepository = userRepository;
		this.modelMapper = modelMapper;
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDto user = getUserByUsername(username);
		if (user != null) {
			List<GrantedAuthority> grantList = new ArrayList<GrantedAuthority>();
			GrantedAuthority authority = new SimpleGrantedAuthority(user.getRole().getName());
			grantList.add(authority);
			UserDetails userDetails = new org.springframework.security.core.userdetails.User(user.getUsername(),
					encrytePassword(user.getPassword()), grantList);
			return userDetails;
		}

		return null;
	}

	@Override
	public List<UserDto> findAll() {
		return userRepository.findAll().stream().map(entity -> modelMapper.map(entity, UserDto.class))
				.collect(Collectors.toList());
	}

	@Override
	public UserDto getUser(@NotNull Integer id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found!"));

		return modelMapper.map(user, UserDto.class);
	}

	@Override
	public Integer createUser(@NotNull @Valid UserDto userDto) {
		User user = modelMapper.map(userDto, User.class);
		user = userRepository.save(user);
		return user.getId();
	}

	@Override
	public void updateUser(@NotNull @Valid UserDto userDto, @NotNull Integer id) {
		User user = userRepository.findById(id).orElseThrow(() -> new RuntimeException("Not found!"));
		user.setRole(new Role());
		modelMapper.map(userDto, user);
		userRepository.save(user);

	}

	@Override
	public void deleteUser(@NotNull Integer id) {
		userRepository.findById(id).ifPresent(userRepository::delete);

	}

	@Override
	public UserDto getUserByUsername(String username) {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found!"));
		return modelMapper.map(user, UserDto.class);
	}

	public static String encrytePassword(String password) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder.encode(password);
	}

	@Override
	public List<UserDto> fndByRole(String roleName) {
		return userRepository.getUsersByRole(roleName).stream().map(entity -> modelMapper.map(entity, UserDto.class))
				.collect(Collectors.toList());
	}

}
