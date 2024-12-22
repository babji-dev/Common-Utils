package com.commonUtils.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import com.commonUtils.dto.RegexRequest;

@Controller
@RequestMapping("/regex-tester")
public class RegexTesterController {
	
	@PostMapping("/test")
    public ResponseEntity<Map<String, Object>> testRegex(@RequestBody RegexRequest request) {
        Map<String, Object> response = new HashMap<>();
        try {
        	int flags = request.getFlags();
        	if((request.getPattern()==null || request.getPattern().isBlank() || request.getText()==null || request.getText().isBlank())) {
        		response.put("valid", false);
                response.put("error", "Pattern or text Shouldn't be Empty!");
        	}else {
        		Pattern pattern = Pattern.compile(request.getPattern().trim(), flags);
                Matcher matcher = pattern.matcher(request.getText());

                List<String> matches = new ArrayList<>();
                while (matcher.find()) {
                    matches.add(matcher.group());
                }
                boolean valid = !matches.isEmpty();
                if(valid)
                	response.put("matches", matches);
                else
                	response.put("error", "Text Doesn't Match with Pattern");
                response.put("valid", valid);
        	}
        } catch (PatternSyntaxException e) {
            response.put("valid", false);
            response.put("error", "Invalid Pattern Provided");
        }
        return ResponseEntity.ok(response);
    }
	
	@GetMapping
	public String regexTester() {
		return "Regex-Utils/Regex-Tester";
	}
	
}
