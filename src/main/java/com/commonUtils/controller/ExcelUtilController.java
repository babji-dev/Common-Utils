package com.commonUtils.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class ExcelUtilController {
	
	@GetMapping("/excel-Splitter")
	public String excelSplitter() {
		return "Excel-Utils/ExcelSplitter";
	}
	
	@PostMapping("/excel-Splitter")
	public String excelSplitterProcesser(@RequestParam("file") MultipartFile file,
            @RequestParam("columns") String columns,
            @RequestParam("outputFormat") String outputFormat) {
		return "Excel-Utils/ExcelSplitter";
	}

}
