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
import com.museum.management.service.RoleService;

@Controller
public class RoleController {

	private final RoleService roleService;
	private final ValidatorFactory validatorFactory;

	public RoleController(RoleService roleService, ValidatorFactory validatorFactory) {
		this.roleService = roleService;
		this.validatorFactory = validatorFactory;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/roles")
	public String getRoles(Model model) {
		model.addAttribute("roles", roleService.findAll());
		return "roles";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/role/{id}")
	public String getRole(Model model, @PathVariable Integer id) {
		RoleDto roleDto = roleService.getRole(id);
		model.addAttribute("role", roleDto);
		return "role";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = { "/role/create", "/role/{id}/edit" })
	public String displayRoleForm(Model model, @PathVariable(required = false) Integer id) {
		RoleDto roleDto = id != null ? roleService.getRole(id) : new RoleDto();
		model.addAttribute("role", roleDto);
		return "role-form";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/role/{id}/delete")
	public String deleteRole(@PathVariable Integer id) {
		roleService.deleteRole(id);
		return "redirect:/roles";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = { "/role/" })
	public String createRole(@ModelAttribute RoleDto roleDto, Model model) {
		if (!isValid(roleDto, model)) {
			return "role-form";
		}
		roleService.createRole(roleDto);
		return "redirect:/roles";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/role/{id}")
	public String updateRole(@ModelAttribute RoleDto roleDto, @PathVariable Integer id, Model model) {
		if (!isValid(roleDto, model)) {
			return "role-form";
		}
		roleService.updateRole(roleDto, id);
		return "redirect:/roles";
	}

	private boolean isValid(RoleDto roleDto, Model model) {
		Set<ConstraintViolation<RoleDto>> constraintViolations = validatorFactory.getValidator().validate(roleDto);

		if (!constraintViolations.isEmpty()) {
			model.addAttribute("role", roleDto);
			model.addAttribute("errors", constraintViolations.stream()
					.map(cv -> cv.getPropertyPath() + " " + cv.getMessage()).collect(Collectors.toList()));

			return false;
		}

		return true;
	}
}
