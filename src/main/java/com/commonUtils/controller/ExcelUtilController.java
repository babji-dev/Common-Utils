package com.commonUtils.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.commonUtils.enums.ExcelOutputFormat;
import com.commonUtils.utils.ExcelSplitter;

@Controller
public class ExcelUtilController {

	@GetMapping("/excel-Splitter")
	public String excelSplitter() {
		return "Excel-Utils/ExcelSplitter";
	}

	@PostMapping("/excel-Splitter")
	public Object  excelSplitterProcesser(@RequestParam MultipartFile file,
			@RequestParam String columns,
			@RequestParam ExcelOutputFormat outputFormat,Model model) throws IOException {

		try {
			if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
				return ResponseEntity.badRequest().body(null);
			}

			Map<String, Workbook> splittedFiles = ExcelSplitter.splitFileByColumn(file, columns, outputFormat);


			if(ExcelOutputFormat.MULTIPLE_FILES.equals(outputFormat)) {
				ByteArrayOutputStream zipOutputStream = ExcelSplitter.createZipFromWorkbooks(splittedFiles);
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=split-files.zip")
						.contentType(MediaType.APPLICATION_OCTET_STREAM)
						.body(zipOutputStream.toByteArray());
			}else  {
				ByteArrayOutputStream zipOutputStream = ExcelSplitter.createZipFromWorkbooks(splittedFiles);
				return ResponseEntity.ok()
						.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=SAME_FILE_WITH_MULTIPLE_SHEETS.zip")
						.contentType(MediaType.APPLICATION_OCTET_STREAM)
						.body(zipOutputStream.toByteArray());
			}


		}catch(Exception e) {
			System.err.println("Error processing file: " + e.getMessage());

			model.addAttribute("errorMessage", e.getMessage());

			return "Excel-Utils/ExcelSplitter";
		}


	}

}
