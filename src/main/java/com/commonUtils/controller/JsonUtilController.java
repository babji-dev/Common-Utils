package com.commonUtils.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class JsonUtilController {
	
	@GetMapping("/json-compare")
	public String JsonComparator() {
		return "Json-Utils/json-compare";
	}

}
