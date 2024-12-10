package com.commonUtils.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.commonUtils.utils.ExcelSplitter;

@Controller
public class ExcelUtilController {
	
	@GetMapping("/excel-Splitter")
	public String excelSplitter() {
		return "Excel-Utils/ExcelSplitter";
	}
	
	@PostMapping("/excel-Splitter")
	public ResponseEntity<byte[]>  excelSplitterProcesser(@RequestParam MultipartFile file,
            @RequestParam String columns,
            @RequestParam String outputFormat) throws IOException {
		
		if (file.isEmpty() || !file.getOriginalFilename().endsWith(".xlsx")) {
            return ResponseEntity.badRequest().body(null);
        }
		
		Map<String, Workbook> splittedFiles = ExcelSplitter.splitFileByColumn(file, columns, outputFormat);
		
		// If multiple files, zip them
        if ("multiple".equalsIgnoreCase(outputFormat)) {
            ByteArrayOutputStream zipOutputStream = ExcelSplitter.createZipFromWorkbooks(splittedFiles);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=split-files.zip")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(zipOutputStream.toByteArray());
        } else {
            // Return single file
            Workbook workbook = splittedFiles.values().iterator().next();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            workbook.write(outputStream);
            workbook.close();
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=split-file.xlsx")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(outputStream.toByteArray());
        }
	}

}
