package com.museum.management.api;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;
import javax.validation.ConstraintViolation;
import javax.validation.ValidatorFactory;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.itextpdf.text.DocumentException;
import com.museum.management.dto.ExhibitDto;
import com.museum.management.print.ExportPfdExhibits;
import com.museum.management.service.ExhibitService;

@Controller
public class ExhibitController {

	private final ExhibitService exhibitService;
	private final ValidatorFactory validatorFactory;

	public ExhibitController(ExhibitService exhibitService, ValidatorFactory validatorFactory) {
		this.exhibitService = exhibitService;
		this.validatorFactory = validatorFactory;
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE', 'ROLE_GUARDIAN')")
    @GetMapping("/exhibits/export")
    public void exportToPDF(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentDateTime = dateFormatter.format(new Date());

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=exhibits_" + currentDateTime + ".pdf";
        response.setHeader(headerKey, headerValue);

        ExportPfdExhibits export = new ExportPfdExhibits(exhibitService.findAll());
        export.exportExhibitsToPdf(response.getOutputStream());
    }
    
	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE', 'ROLE_GUARDIAN')")
	@GetMapping("/exhibits")
	public String getExhibits(Model model) {
		model.addAttribute("exhibits", exhibitService.findAll());
		return "exhibits";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE', 'ROLE_GUARDIAN')")
	@GetMapping("/exhibit/{id}")
	public String getExhibit(Model model, @PathVariable Integer id) {
		ExhibitDto exhibitDto = exhibitService.getExhibit(id);
		model.addAttribute("exhibit", exhibitDto);
		return "exhibit";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
	@GetMapping(value = { "/exhibit/create", "/exhibit/{id}/edit" })
	public String displayExhibitForm(Model model, @PathVariable(required = false) Integer id) {
		ExhibitDto exhibitDto = id != null ? exhibitService.getExhibit(id) : new ExhibitDto();
		model.addAttribute("exhibit", exhibitDto);
		return "exhibit-form";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
	@GetMapping("/exhibit/{id}/delete")
	public String deteleExhibit(@PathVariable Integer id) {
		exhibitService.deleteExhibit(id);
		return "redirect:/exhibits";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
	@PostMapping(value = { "/exhibit/" })
	public String createExhibit(@ModelAttribute ExhibitDto exhibitDto, Model model) {
		if (!isValid(exhibitDto, model)) {
			return "exhibit-form";
		}
		exhibitService.createExhibit(exhibitDto);
		return "redirect:/exhibits";
	}

	@PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_EMPLOYEE')")
	@PostMapping("/exhibit/{id}")
	public String updateExhibit(@ModelAttribute ExhibitDto exhibitDto, @PathVariable Integer id, Model model) {
		if (!isValid(exhibitDto, model)) {
			return "exhibit-form";
		}
		exhibitService.updateExhibit(exhibitDto, id);
		return "redirect:/exhibits";
	}

	private boolean isValid(ExhibitDto exhibitDto, Model model) {
		Set<ConstraintViolation<ExhibitDto>> constraintViolations = validatorFactory.getValidator()
				.validate(exhibitDto);

		if (!constraintViolations.isEmpty()) {
			model.addAttribute("exhibit", exhibitDto);
			model.addAttribute("errors", constraintViolations.stream()
					.map(cv -> cv.getPropertyPath() + " " + cv.getMessage()).collect(Collectors.toList()));

			return false;
		}

		return true;
	}
}
