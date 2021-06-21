package com.museum.management.api;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class ApplicationController {

	@GetMapping("")
	public String getHomePage() {
		return "homepage";
	}
}
