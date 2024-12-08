package com.commonUtils.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AppEntryController {
	
	@GetMapping("/")
	public String home() {
		return "Home";
	}

}
