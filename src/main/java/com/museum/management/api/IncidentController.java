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

import com.museum.management.dto.ExhibitDto;
import com.museum.management.dto.IncidentDto;
import com.museum.management.dto.UserDto;
import com.museum.management.service.ExhibitService;
import com.museum.management.service.IncidentService;
import com.museum.management.service.UserService;

@Controller
public class IncidentController {

	private final IncidentService incidentService;
	private final UserService userService;
	private final ExhibitService exhibitService;
	private final ValidatorFactory validatorFactory;

	public IncidentController(UserService userService, ValidatorFactory validatorFactory,
			IncidentService incidentService, ExhibitService exhibitService) {
		this.userService = userService;
		this.incidentService = incidentService;
		this.exhibitService = exhibitService;
		this.validatorFactory = validatorFactory;
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@GetMapping("/incidents")
	public String getIncidents(Model model) {
		model.addAttribute("incidents", incidentService.findAll());
		return "incidents";
	}

	@PreAuthorize("hasRole('ROLE_GUARDIAN')")
	@GetMapping("/myIncidents")
	public String getMyIncidents(Model model) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		model.addAttribute("incidents", incidentService.getMyIncident(authentication.getName()));
		return "myIncidents";
	}

	@PreAuthorize("hasRole('ROLE_GUARDIAN')")
	@GetMapping("/incident/{id}")
	public String getIncident(Model model, @PathVariable Integer id) {
		IncidentDto incidentDto = incidentService.getIncident(id);
		model.addAttribute("incident", incidentDto);
		return "incident";
	}

	@PreAuthorize("hasRole('ROLE_GUARDIAN')")
	@GetMapping(value = { "/incident/create", "/incident/{id}/edit" })
	public String displayIncidentForm(Model model, @PathVariable(required = false) Integer id) {
		IncidentDto incidentDto = id != null ? incidentService.getIncident(id) : new IncidentDto();
		model.addAttribute("incident", incidentDto);
		addAvailableExhibits(model);
		return "incident-form";
	}

	@PreAuthorize("hasRole('ROLE_GUARDIAN')")
	@GetMapping("/incident/{id}/delete")
	public String deleteIncident(@PathVariable Integer id) {
		incidentService.deleteIncident(id);
		return "redirect:/myIncidents";
	}

	@PreAuthorize("hasRole('ROLE_GUARDIAN')")
	@PostMapping(value = { "/incident/" })
	public String createIncident(@ModelAttribute IncidentDto incidentDto, Model model) {
		if (!isValid(incidentDto, model)) {
			addAvailableExhibits(model);
			return "incident-form";
		}
		setIncidentFields(incidentDto);
		incidentService.createIncident(incidentDto);
		return "redirect:/myIncidents";
	}

	@PreAuthorize("hasRole('ROLE_GUARDIAN')")
	@PostMapping("/incident/{id}")
	public String updateIncident(@ModelAttribute IncidentDto incidentDto, @PathVariable Integer id, Model model) {
		if (!isValid(incidentDto, model)) {
			addAvailableExhibits(model);
			return "incident-form";
		}
		setIncidentFields(incidentDto);
		incidentService.updateIncident(incidentDto, id);
		return "redirect:/myIncidents";
	}

	private void setIncidentFields(IncidentDto incidentDto) {
		if (incidentDto.getSelectedExhibitId() != null) {
			ExhibitDto exhibit = exhibitService.getExhibit(incidentDto.getSelectedExhibitId());
			incidentDto.setExhibit(exhibit);
		}
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		UserDto user = userService.getUserByUsername(authentication.getName());
		incidentDto.setUser(user);
	}

	private void addAvailableExhibits(Model model) {
		model.addAttribute("availableExhibits", exhibitService.findAll());
	}

	private boolean isValid(IncidentDto incidentDto, Model model) {
		Set<ConstraintViolation<IncidentDto>> constraintViolations = validatorFactory.getValidator()
				.validate(incidentDto);

		if (!constraintViolations.isEmpty()) {
			model.addAttribute("incident", incidentDto);
			model.addAttribute("errors", constraintViolations.stream()
					.map(cv -> cv.getPropertyPath() + " " + cv.getMessage()).collect(Collectors.toList()));

			return false;
		}

		return true;
	}
}
