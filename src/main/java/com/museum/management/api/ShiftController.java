package com.museum.management.api;

import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.museum.management.dto.ShiftDto;
import com.museum.management.dto.UserDto;
import com.museum.management.service.ShiftService;
import com.museum.management.service.UserService;

@Controller
public class ShiftController {

	private final UserService userService;
	private final ShiftService shiftService;
	private final ValidatorFactory validatorFactory;

	public ShiftController(UserService userService, ValidatorFactory validatorFactory, ShiftService shiftService) {
		this.userService = userService;
		this.shiftService = shiftService;
		this.validatorFactory = validatorFactory;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/shifts")
	public String getShifts(Model model) {
		model.addAttribute("shifts", shiftService.findAll());
		return "shifts";
	}

	@PreAuthorize("hasRole('ROLE_GUARDIAN')")
	@GetMapping("/myShifts")
	public String getMyShifts(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("shifts", shiftService.getMyShifts(authentication.getName()));
		return "myShifts";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/shift/{id}")
	public String getShift(Model model, @PathVariable Integer id) {
		ShiftDto shiftDto = shiftService.getShift(id);
		model.addAttribute("shift", shiftDto);
		return "shift";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping(value = { "/shift/create", "/shift/{id}/edit" })
	public String displayshiftForm(Model model, @PathVariable(required = false) Integer id) {
		ShiftDto shiftDto = id != null ? shiftService.getShift(id) : new ShiftDto();
		model.addAttribute("shift", shiftDto);
		addAvailableUsers(model);
		return "shift-form";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/shift/{id}/delete")
	public String deleteShift(@PathVariable Integer id) {
		shiftService.deleteShift(id);
		return "redirect:/shifts";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping(value = { "/shift/" })
	public String createShift(@ModelAttribute ShiftDto shiftDto, Model model) {
		if (!isValid(shiftDto, model)) {
			addAvailableUsers(model);
			return "shift-form";
		}
		setUserToShift(shiftDto);
		shiftService.createShift(shiftDto);
		return "redirect:/shifts";
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@PostMapping("/shift/{id}")
	public String updateShift(@ModelAttribute ShiftDto shiftDto, @PathVariable Integer id, Model model) {
		if (!isValid(shiftDto, model)) {
			addAvailableUsers(model);
			return "shift-form";
		}
		setUserToShift(shiftDto);
		shiftService.updateShift(shiftDto, id);
		return "redirect:/shifts";
	}

	private void addAvailableUsers(Model model) {
		model.addAttribute("availableUsers", userService.fndByRole("ROLE_GUARDIAN"));
	}

	private void setUserToShift(ShiftDto shiftDto) {
		if (shiftDto.getSelectedUserId() != null) {
			UserDto user = userService.getUser(shiftDto.getSelectedUserId());
			shiftDto.setUser(user);
		}
	}

	private boolean isValid(ShiftDto shiftDto, Model model) {
		Set<ConstraintViolation<ShiftDto>> constraintViolations = validatorFactory.getValidator().validate(shiftDto);

		if (!constraintViolations.isEmpty()) {
			model.addAttribute("shift", shiftDto);
			model.addAttribute("errors", constraintViolations.stream()
					.map(cv -> cv.getPropertyPath() + " " + cv.getMessage()).collect(Collectors.toList()));

			return false;
		}

		return true;
	}
}
