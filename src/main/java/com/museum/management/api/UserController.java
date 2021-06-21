package com.museum.management.api;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.museum.management.dto.RoleDto;
import com.museum.management.dto.UserDto;
import com.museum.management.service.RoleService;
import com.museum.management.service.UserService;

@Controller
public class UserController {

	private final UserService userService;
	private final RoleService roleService;
	private final ValidatorFactory validatorFactory;

	public UserController(UserService userService, ValidatorFactory validatorFactory, RoleService roleService) {
		this.userService = userService;
		this.roleService = roleService;
		this.validatorFactory = validatorFactory;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/users")
	public String getUsers(Model model) {
		model.addAttribute("users", userService.findAll());
		return "users";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/user/{id}")
	public String getUser(Model model, @PathVariable Integer id) {
		UserDto userDto = userService.getUser(id);
		model.addAttribute("user", userDto);
		return "user";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = { "/user/create", "/user/{id}/edit" })
	public String displayUserForm(Model model, @PathVariable(required = false) Integer id) {
		UserDto userDto = id != null ? userService.getUser(id) : new UserDto();
		model.addAttribute("user", userDto);
		addAvailableRoles(model);
		return "user-form";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/user/{id}/delete")
	public String deteleUser(@PathVariable Integer id) {
		userService.deleteUser(id);
		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = { "/user/" })
	public String createUser(@ModelAttribute UserDto userDto, Model model) {
		if (!isValid(userDto, model)) {
			addAvailableRoles(model);
			return "user-form";
		}
		setRoleToUser(userDto);
		userService.createUser(userDto);
		return "redirect:/users";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/user/{id}")
	public String updateUser(@ModelAttribute UserDto userDto, @PathVariable Integer id, Model model) {
		if (!isValid(userDto, model)) {
			addAvailableRoles(model);
			return "user-form";
		}
		setRoleToUser(userDto);
		userService.updateUser(userDto, id);
		return "redirect:/users";
	}

	private void addAvailableRoles(Model model) {
		model.addAttribute("availableRoles", roleService.findAll());
	}

	private void setRoleToUser(UserDto userDto) {
		if (userDto.getSelectedRoleId() != null) {
			RoleDto role = roleService.getRole(userDto.getSelectedRoleId());
			userDto.setRole(role);
		}
	}

	private boolean isValid(UserDto userDto, Model model) {
		Set<ConstraintViolation<UserDto>> constraintViolations = validatorFactory.getValidator().validate(userDto);

		if (!constraintViolations.isEmpty()) {
			model.addAttribute("user", userDto);
			model.addAttribute("errors", constraintViolations.stream()
					.map(cv -> cv.getPropertyPath() + " " + cv.getMessage()).collect(Collectors.toList()));

			return false;
		}

		return true;
	}
}
