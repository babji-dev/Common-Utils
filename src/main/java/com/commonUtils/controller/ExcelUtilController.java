package com.commonUtils.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ExcelUtilController {
	
	@GetMapping("/excel-Splitter")
	public String excelSplitter() {
		return "Excel-Utils/ExcelSplitter";
	}

}
